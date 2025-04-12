package com.bubble.buubleforprofessor.domain.payment.repository;

import com.bubble.buubleforprofessor.domain.payment.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
