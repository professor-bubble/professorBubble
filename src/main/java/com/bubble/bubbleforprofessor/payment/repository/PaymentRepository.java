package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.order.orderId IN :orderIds")
    void hardDeleteByOrderIds(@Param("orderIds") List<Long> orderIds);
}
