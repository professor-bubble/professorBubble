package com.bubble.buubleforprofessor.domain.payment.repository;

import com.bubble.buubleforprofessor.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepsository extends JpaRepository<Payment, Long> {
}
