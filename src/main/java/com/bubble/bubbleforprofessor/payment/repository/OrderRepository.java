package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.Order;
import com.bubble.bubbleforprofessor.payment.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status AND o.createdAt < :cutoff AND o.isDeleted = false")
    List<Order> findExpiredOrders(@Param("status") OrderStatus status, @Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status AND o.createdAt < :cutoff AND o.isDeleted = false")
    Page<Order> findByOrderStatusAndCreatedAtBeforeAndIsDeletedFalse(
            @Param("status") OrderStatus status,
            @Param("cutoff") LocalDateTime cutoff,
            Pageable pageable
    );


    @Modifying
    @Query("DELETE FROM Order o WHERE o.orderId IN :ids")
    void hardDeleteByOrderIds(@Param("ids") List<Long> ids);


    @Query("SELECT o.orderId FROM Order o WHERE o.isDeleted = true AND o.deletedAt < :cutoff")
    List<Long> findIdsBySoftDeletedBefore(@Param("cutoff") LocalDateTime cutoff);

}
