package com.bubble.bubbleforprofessor.payment.scheduler;

import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderStatus;
import com.bubble.bubbleforprofessor.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.payment.repository.PaymentRepository;
import com.bubble.bubbleforprofessor.payment.service.PaymentRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCleanupScheduler {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRedisService redisService;

    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분 간격
    @Transactional
    public void cleanUpExpiredPendingPayments() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15); //15분 전 시간

        List<Order> expiredOrders = orderRepository.findExpiredOrders(OrderStatus.PENDING, cutoff);

        expiredOrders.forEach(this :: softDeleteOrderFlow);
    }

    private void softDeleteOrderFlow(Order order) {
        log.info("[SoftDelete] orderId={} soft deleting...", order.getOrderId());

        // 1. 주문 soft delete
        order.softDelete();
        orderRepository.save(order);

        // 2. 주문 상세(OrderDetail) soft delete
        orderDetailRepository.softDeleteByOrderId(order.getOrderId());

        // 3. 결제(Payment) soft delete
        paymentRepository.findByOrder(order).ifPresent(payment -> {
            payment.softDelete();
            paymentRepository.save(payment);
        });

        String redisKey = "ORDER_" + order.getOrderId();
        redisService.deleteRedisOrder(redisKey);

        log.info("[SoftDelete] orderId={}및 관련 orderDetail/payment softdelet 완료", order.getOrderId());
    }
}