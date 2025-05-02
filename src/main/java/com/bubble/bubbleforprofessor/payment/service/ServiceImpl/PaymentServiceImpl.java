package com.bubble.bubbleforprofessor.payment.service.ServiceImpl;

import com.bubble.bubbleforprofessor.payment.Client.TossClient;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderDetailRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.*;
import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderDetail;
import com.bubble.bubbleforprofessor.payment.entity.OrderStatus;
import com.bubble.bubbleforprofessor.payment.entity.Payment;
import com.bubble.bubbleforprofessor.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.payment.repository.PaymentRepository;
import com.bubble.bubbleforprofessor.payment.service.PaymentRedisService;
import com.bubble.bubbleforprofessor.payment.service.PaymentService;
import com.bubble.bubbleforprofessor.skin.entity.Skin;
import com.bubble.bubbleforprofessor.skin.repository.SkinRepository;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentRedisService redisService;
    private final SkinRepository skinRepository;
    private final UserRepository userRepository;
    private final TossClient tossClient;

    /**
     * 1) 내부 DB/Redis에 PENDING 상태로 저장
     * 2) TossPayments ready 호출 후 클라이언트에 결제URL 반환
     */
    @Override
    public InitTossResponseDto initPayment(OrderRequestDto dto, CustomPrincipal principal) {

        // 내부
        InitPaymentResponseDto init = prepareInternal(dto, principal);

        // TossPayment경우 orderId 타입이 String이라 변환 필요
        String orderId = String.valueOf(init.getOrderId());

        // TossPayments 준비 요청
        TossInitResponseDto tossResp = tossClient.ready(
                orderId,
                init.getAmount()
        );

        // 클라이언트에 최종 DTO 반환
        return InitTossResponseDto.builder()
                .orderId(orderId)
                .amount(init.getAmount())
                .paymentKey(tossResp.getPaymentKey())
                .build();
    }

    /**
     * Order, OrderDetail, Payment 엔티티 저장 및 Redis 보관
     */
    @Transactional
    public InitPaymentResponseDto prepareInternal(OrderRequestDto orderRequestDto,
                                                   CustomPrincipal customPrincipal) {
        // 1. 사용자 조회
        User user = userRepository.findById(UUID.fromString(customPrincipal.getUserId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        // 2. 요청에서 skinId 리스트 추출
        List<Integer> skinIds = orderRequestDto.getItems().stream()
                .map(OrderDetailRequestDto::getSkinId)
                .toList();

        // 3. Skin 정보 미리 조회 (price 확인용)
        List<Skin> skins = skinRepository.findAllById(skinIds);
        Map<Integer, Skin> skinMap = skins.stream()
                .collect(Collectors.toMap(Skin::getId, Function.identity()));

        // 4. Order 먼저 생성 및 저장 (orderId 확보)
        Order order = Order.builder()
                .user(user)
                .totalAmount(0) // 나중에 업데이트할 예정
                .build();
        Order savedOrder = orderRepository.save(order);

        // 5. OrderDetail 생성 및 저장
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetailRequestDto item : orderRequestDto.getItems()) {
            Skin skin = skinMap.get(item.getSkinId());
            if (skin == null || skin.isDelete()) {
                throw new CustomException(ErrorCode.NON_EXISTENT_SKIN);
            }

            details.add(OrderDetail.builder()
                    .order(savedOrder)
                    .skin(skin)
                    .price(skin.getPrice())
                    .quantity(item.getQuantity())
                    .build());
        }
        orderDetailRepository.saveAll(details);

        // 6. DB에서 총 결제 금액 계산 (SUM(price * quantity))
        int totalAmount = orderDetailRepository.calculateTotalAmount(savedOrder.getOrderId());

        // 7. Order에 금액 반영 후 저장
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        // 8. Payment 생성 및 저장
        Payment payment = Payment.builder()
                .order(savedOrder)
                .amount(totalAmount)
                .paymentKey("TEMP_" + savedOrder.getOrderId())
                .paymentMethod("PENDING")
                .build();
        paymentRepository.save(payment);

        // 9. Redis에 결제 금액 저장 (1800초 = 30분 TTL)
        String redisKey = "ORDER_" + savedOrder.getOrderId();
        redisService.saveOrderAmount(redisKey, totalAmount, 1800);

        // 10. 사용자에게 반환
        return InitPaymentResponseDto.builder()
                .orderId(String.valueOf(savedOrder.getOrderId())) // orderID는 long 타입이라 형변환 필요
                .amount(totalAmount)
                .build();
    }


    @Transactional
    @Override
    public SuccessResponseDto completePayment(String paymentKey, Long orderId, int amount) {

        String tossOrderId = String.valueOf(orderId);

        // 토스페이먼츠 서버에 결제 승인 요청
        TossConfirmResponseDto tossResponse = tossClient.confirm(paymentKey, tossOrderId, amount);

        // Redis의 orderid의 amount 값을 불러옴
        String redisOrderId = "ORDER_" + orderId;
        Integer redisOrderAmount = redisService.getOrderAmount(redisOrderId);
        if (redisOrderAmount == null) {
            throw new CustomException(ErrorCode.NON_EXISTENT_ORDER);
        }

        if(!redisOrderAmount.equals(tossResponse.getTotalAmount())) {
           // failPayment(paymentKey, orderId, amount);
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }

        String paymentStatus = tossResponse.getStatus();
        String paymentTime = tossResponse.getApprovedAt();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_ORDER));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_PAYMENT));

        payment.updatePaymentStatus(paymentStatus, paymentTime);

        // 5. Order 상태 업데이트
        if ("DONE".equals(paymentStatus)) {
            order.updateOrderStatus(OrderStatus.SUCCEEDED);
        } else if ("CANCELED".equals(paymentStatus)) {
            order.updateOrderStatus(OrderStatus.CANCELED);
        } else if ("FAILED".equals(paymentStatus)) {
            order.updateOrderStatus(OrderStatus.FAILED);
        } else {
            throw new CustomException(ErrorCode.UNKNOWN_PAYMENT_STATUS);
        }

        paymentRepository.save(payment);
        orderRepository.save(order);

        redisService.deleteRedisOrder(redisOrderId);

        return new SuccessResponseDto(paymentStatus, String.valueOf(orderId), paymentKey, amount, paymentStatus);
    }

    @Override
    public void failPayment(String paymentKey, String orderId, int amount) {
        // 결제 실패 콜백 처리
    }
}
