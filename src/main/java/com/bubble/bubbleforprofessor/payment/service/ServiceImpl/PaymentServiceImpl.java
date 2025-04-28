package com.bubble.bubbleforprofessor.payment.service.ServiceImpl;

import com.bubble.bubbleforprofessor.payment.Client.TossClient;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderDetailRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitPaymentResponseDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitTossResponseDto;
import com.bubble.bubbleforprofessor.payment.dto.response.TossInitResponseDto;
import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderDetail;
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

import java.time.LocalDateTime;
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

        // TossPayments 준비 요청
        TossInitResponseDto tossResp = tossClient.ready(
                init.getOrderId(),
                init.getAmount()
        );

        // 클라이언트에 최종 DTO 반환
        return InitTossResponseDto.builder()
                .orderId(init.getOrderId())
                .amount(init.getAmount())
                .paymentKey(tossResp.getPaymentKey())
                .checkoutUrl(tossResp.getCheckoutUrl())
                .build();
    }

    /**
     * Order, OrderDetail, Payment 엔티티 저장 및 Redis 보관
     */
    private InitPaymentResponseDto prepareInternal(OrderRequestDto orderRequestDto,
                                                   CustomPrincipal customPrincipal) {
        // 1. User 조회
        User user = userRepository.findById(UUID.fromString(customPrincipal.getUserId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        // 2. skinId 리스트 추출
        List<Integer> skinIds = orderRequestDto.getItems().stream()
                .map(OrderDetailRequestDto::getSkinId)
                .toList();

        // 3. Skin 조회 및 맵 변환
        List<Skin> skins = skinRepository.findAllById(skinIds);
        Map<Integer, Skin> skinMap = skins.stream()
                .collect(Collectors.toMap(Skin::getId, Function.identity()));

        // 4. Order 생성
        Order order = Order.builder().user(user).totalAmount(0).build();
        Order savedOrder = orderRepository.save(order);

        // 5. OrderDetail 생성 및 가격 계산
        int totalAmount = 0;
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetailRequestDto item : orderRequestDto.getItems()) {
            Skin skin = skinMap.get(item.getSkinId());
            if (skin == null || skin.isDelete()) {
                throw new CustomException(ErrorCode.NON_EXISTENT_SKIN);
            }
            int itemPrice = skin.getPrice() * item.getQuantity();
            totalAmount += itemPrice;
            details.add(OrderDetail.builder()
                    .order(savedOrder)
                    .skin(skin)
                    .price(skin.getPrice())
                    .quantity(item.getQuantity())
                    .build());
        }
        orderDetailRepository.saveAll(details);

        // 6. totalAmount 업데이트
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        // 7. Payment(PENDING) 생성 및 저장
        Payment payment = Payment.builder()
                .order(savedOrder)
                .amount(totalAmount)
                .paymentKey("TEMP_" + savedOrder.getOrderId())
                .paymentMethod("PENDING")
                .build();
        paymentRepository.save(payment);

        // 8. Redis 저장
        String orderId = "ORDER_" + savedOrder.getOrderId();
        redisService.saveOrderAmount(orderId, totalAmount, 1800);

        // 9. InitPaymentResponseDto 반환
        return InitPaymentResponseDto.builder()
                .orderId(orderId)
                .amount(totalAmount)
                .build();
    }

    @Override
    public void completePayment(String paymentKey, String orderId, int amount) {
        // 결제 성공 콜백 처리
        // (웹훅/클라이언트로부터 받은 정보 검증 및 Payment.updateStatus 등)
    }

    @Override
    public void failPayment(String paymentKey, String orderId, int amount) {
        // 결제 실패 콜백 처리
    }
}
