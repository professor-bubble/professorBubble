package com.bubble.bubbleforprofessor.domain.payment.repository;

import com.bubble.bubbleforprofessor.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
