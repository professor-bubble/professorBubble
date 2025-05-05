package com.bubble.bubbleforprofessor.payment;

import com.bubble.bubbleforprofessor.payment.Client.TossClient;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderDetailRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitTossResponseDto;
import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.Payment;
import com.bubble.bubbleforprofessor.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.payment.repository.PaymentRepository;
import com.bubble.bubbleforprofessor.payment.service.PaymentRedisService;
import com.bubble.bubbleforprofessor.payment.service.ServiceImpl.PaymentServiceImpl;
import com.bubble.bubbleforprofessor.skin.entity.Skin;
import com.bubble.bubbleforprofessor.skin.repository.SkinRepository;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRedisService redisService;

    @Mock
    private SkinRepository skinRepository;

    @Mock
    private TossClient tossClient;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("결제 초기화 요청시, 주문과 결제 엔티티가 생성된다")
    void initPayment_결제초기화_작동확인() {
        // given
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .loginId("testLoginId")
                .password("testPassword")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastLoginAt(Timestamp.valueOf(LocalDateTime.now()))
                .name("테스트유저")
                .phoneNumber("010-1234-5678")
                .email("test@email.com")
                .university(null)
                .role(null)
                .build();

        CustomPrincipal customPrincipal = new CustomPrincipal(user);

        List<OrderDetailRequestDto> items = List.of(
                OrderDetailRequestDto.builder()
                        .skinId(1)
                        .quantity(2)
                        .build(),
                OrderDetailRequestDto.builder()
                        .skinId(2)
                        .quantity(1)
                        .build()
        );


        OrderRequestDto requestDto = OrderRequestDto.builder()
                .items(items)
                .build();

        Skin skin1 = Skin.builder()
                .name("스킨1")
                .price(1000)
                .description("테스트 스킨1")
                .isDelete(false)
                .category(null)
                .build();

        Skin skin2 = Skin.builder()
                .name("스킨2")
                .price(2000)
                .description("테스트 스킨2")
                .isDelete(false)
                .category(null)
                .build();

        ReflectionTestUtils.setField(skin1, "id",1);
        ReflectionTestUtils.setField(skin2, "id",2);

        //주문
        Order order = Order.builder()
                .user(user)
                .totalAmount(0)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(skinRepository.findAllById(List.of(1, 2))).thenReturn(List.of(skin1, skin2));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(tossClient.ready(anyString(), anyInt()))
                .thenReturn(new TossInitResponseDto("mockPaymentKey", "mockCheckoutUrl"));


        // when
        InitTossResponseDto result = paymentService.initPayment(requestDto, customPrincipal);

        // then
        assertThat(result.getOrderId()).startsWith("ORDER_"); // id 없이 orderId 형식만 확인
        assertThat(result.getAmount()).isEqualTo((1000 * 2) + (2000 * 1)); // 4000 기대

        //verify은 로직이 동작되었는지 확인 1번호출되었는지 확인함
        verify(orderDetailRepository).saveAll(anyList());
        verify(paymentRepository).save(any(Payment.class));
        verify(redisService).saveOrderAmount(any(), eq(4000), anyLong());
    }


}
