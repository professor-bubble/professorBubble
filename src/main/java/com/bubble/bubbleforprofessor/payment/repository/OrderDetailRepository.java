package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Modifying
    @Query("UPDATE OrderDetail od SET od.isDeleted = true, od.deletedAt = CURRENT_TIMESTAMP WHERE od.order.orderId = :orderId")
    void softDeleteByOrderId(@Param("orderId") Long orderId);
}
