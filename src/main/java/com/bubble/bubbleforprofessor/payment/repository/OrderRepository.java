package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderStatusAndCreatedAtBefore(OrderStatus orderStatus, LocalDateTime createdAt);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status AND o.createdAt < :cutoff AND o.isDeleted = false")
    List<Order> findExpiredOrders(@Param("status") OrderStatus status, @Param("cutoff") LocalDateTime cutoff);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.isDeleted = true AND o.deletedAt < :cutoff")
    void hardDeleteOldSoftDeletedOrders(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT o.orderId FROM Order o WHERE o.isDeleted = true AND o.deletedAt < :cutoff")
    List<Long> findIdsBySoftDeletedBefore(@Param("cutoff") LocalDateTime cutoff);

}
