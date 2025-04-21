package com.bubble.bubbleforprofessor.domain.payment.service.ServiceImpl;

import com.bubble.bubbleforprofessor.domain.payment.dto.request.OrderDetailRequestDto;
import com.bubble.bubbleforprofessor.domain.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.domain.payment.dto.response.InitPaymentResponseDto;
import com.bubble.bubbleforprofessor.domain.payment.entity.Order;
import com.bubble.bubbleforprofessor.domain.payment.entity.OrderDetail;
import com.bubble.bubbleforprofessor.domain.payment.entity.Payment;
import com.bubble.bubbleforprofessor.domain.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.domain.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.domain.payment.repository.PaymentRepository;

import com.bubble.bubbleforprofessor.domain.payment.service.PaymentRedisService;
import com.bubble.bubbleforprofessor.domain.skin.entity.Skin;
import com.bubble.bubbleforprofessor.domain.skin.repository.SkinRepository;
import com.bubble.bubbleforprofessor.domain.user.entity.User;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    @Value("${toss.secret-Key}")
    private String tossPaymentKey;

    @Override
    public InitPaymentResponseDto  initPayment(OrderRequestDto orderRequestDto) {
        User user = getCurrentUser();

        // 사용자가 주문한 주문상세 skinId 추출
        List<Long> skinId = orderRequestDto.getItmes().stream()
                .map(OrderDetailRequestDto::getSkinId)
                .collect(Collectors.toList());

        //DB에 있는 스킨 ID 리스트 불러옴
        List<Skin> skins = skinRepository.findAllById(skinId);



        //스킨 리스트 map을 변환하여 검색 성능 최적화
        Map<Long, Skin> skinMap = skins.stream()
                .collect(Collectors.toMap(Skin::getSkinid, Function.identity()));

        List<OrderDetail>  orderDetails = new ArrayList<>();
        int totalAmount = 0;

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .build();

        Order savedOrder = orderRepository.save(order);


        for (OrderDetailRequestDto item : orderRequestDto.getItmes()){
            Skin skin = skinMap.get(item.getSkinId());

            //스킨 유효성 검상
            if(skin == null){
            throw new CustomException(ErrorCode.INVALID_SKIN_ID);
            }

            int itemPrice = skin.getPrice()*item.getQuantity();
            totalAmount  += itemPrice;


            OrderDetail orderDetail = OrderDetail.builder()
                    .skin(skin)
                    .price(skin.getPrice())
                    .quantity(item.getQuantity())
                    .order(savedOrder)
                    .build();
            orderDetails.add(orderDetail);
        }



        for (OrderDetail orderDetail : orderDetails) {
            orderDetailRepository.save(orderDetail);
        }


        Payment payment = Payment.builder()
                .order(savedOrder)
                .amount(totalAmount)

                //이부분 수정 필요!!!
                .paymentKey("temp_key_" + savedOrder.getOrderId()) // 실제 paymentKey를 받기 전이라 임시로 "temp_key_"를 사용
                .paymentMethod("TOSS") //초기값 추후 갱신됨
                .build();
        paymentRepository.save(payment);

        String orderId = "ORDER_" + savedOrder.getOrderId();
        redisService.saveOrderAmount(orderId, totalAmount, 1800);

        return InitPaymentResponseDto.builder()
                .orderId(orderId)
                .amount(totalAmount)
                .build();

    }


    private User getCurrentUser () {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalStateException("JWT 토큰이 없습니다.");
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: userId=" + userId));
    }
}
