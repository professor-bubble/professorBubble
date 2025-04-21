package com.bubble.bubbleforprofessor.domain.payment.repository;

import com.bubble.bubbleforprofessor.domain.payment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
