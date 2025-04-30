package com.bubble.bubbleforprofessor.payment.scheduler;

import com.bubble.bubbleforprofessor.payment.repository.OrderDetailRepository;
import com.bubble.bubbleforprofessor.payment.repository.OrderRepository;
import com.bubble.bubbleforprofessor.payment.repository.PaymentRepository;
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
public class PaymentHardDeleteScheduler {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 매일 새벽 3시에 실행됨
     * 30일이 지난 soft-deleted 주문 관련 데이터 완전 삭제
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void hardDeleteExpiredSoftDeletedOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);

        // 30일 이상 soft delete된 주문 id 조회 ->  불필요한 쿼리 실행을 막기 위함
        List<Long> orderIdsToDelete = orderRepository.findIdsBySoftDeletedBefore(cutoff);
        if (orderIdsToDelete.isEmpty()) {
            log.info("[HardDelete] 삭제 대상 주문 없음");
            return;
        }

        orderDetailRepository.hardDeleteByOrderIds(orderIdsToDelete);
        paymentRepository.hardDeleteByOrderIds(orderIdsToDelete);
        orderRepository.hardDeleteOldSoftDeletedOrders(cutoff);

        log.info("[HardDelete] {}건의 주문/주문상세/결제 정보를 영구 삭제 완료", orderIdsToDelete.size());
    }
}