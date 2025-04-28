package com.bubble.bubbleforprofessor.payment.service.ServiceImpl;

import com.bubble.bubbleforprofessor.payment.dto.request.OrderDetailRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitPaymentResponseDto;
import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderDetail;
import com.bubble.bubbleforprofessor.payment.entity.Payment;
import com.bubble.bubbleforprofessor.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.payment.repository.PaymentRepository;

import com.bubble.bubbleforprofessor.payment.service.PaymentRedisService;
import com.bubble.bubbleforprofessor.skin.entity.Skin;
import com.bubble.bubbleforprofessor.skin.repository.SkinRepository;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;

import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentRedisService redisService;
    private final RestTemplate restTemplate;
    private final SkinRepository skinRepository;
    private final UserRepository userRepository;


    private String tossPaymentKey;

    public InitPaymentResponseDto initPayment(OrderRequestDto orderRequestDto, CustomPrincipal customPrincipal) {
        //1. 유저 조회
        User user = userRepository.findById(UUID.fromString(customPrincipal.getUserId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        //2. 주문 항목에서 skinId리스트 추출
        List<Integer> skinIds = orderRequestDto.getItems().stream()
                .map(OrderDetailRequestDto::getSkinId)
                .toList();

        //3. 스킨 조회 및 map 변환
        List<Skin> skins = skinRepository.findAllById(skinIds);
        Map<Integer, Skin> skinMap = skins.stream()
                .collect(Collectors.toMap(Skin::getId, Function.identity())); //function.identity skin를 값을 그대로 사용

        // 4. Order 생성
        Order order = Order.builder().user(user).totalAmount(0).build();
        Order savedOrder = orderRepository.save(order);

        // 5. OrderDetail 생성 + 가격 계산
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

        //계산되 총 값 order로 갱신
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);


        // 6. Payment 엔티티 생성
        Payment payment = Payment.builder()
                .order(savedOrder)
                .amount(totalAmount)
                .paymentKey("TEMP_" + savedOrder.getOrderId()) // Toss로부터 아직 안 받은 상태
                .paymentMethod("PENDING")
                .build();
        paymentRepository.save(payment);

        // 7. Redis 저장
        String orderId = "ORDER_" + savedOrder.getOrderId();
        redisService.saveOrderAmount(orderId, totalAmount, 1800);

        // 8. 응답
        return InitPaymentResponseDto.builder()
                .orderId(orderId)
                .amount(totalAmount)
                .build();
    }
    }


