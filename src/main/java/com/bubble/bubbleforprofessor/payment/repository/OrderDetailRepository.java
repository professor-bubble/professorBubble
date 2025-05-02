package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    /*
    * Query 어노테이션은 SELECT 용
    * 수정/삭제 쿼리 직접 사용시 Modifying 어노테이션 붙여줘야함
    * */

    @Modifying
    @Query("UPDATE OrderDetail od SET od.isDeleted = true, od.deletedAt = CURRENT_TIMESTAMP WHERE od.order.orderId = :orderId")
    void softDeleteByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.order.orderId IN :orderIds")
    void hardDeleteByOrderIds(@Param("orderIds") List<Long> orderIds);

    @Query("SELECT SUM(od.price * od.quantity) FROM OrderDetail od WHERE od.order.orderId = :orderId")
    Integer calculateTotalAmount(@Param("orderId") Long orderId);
}
