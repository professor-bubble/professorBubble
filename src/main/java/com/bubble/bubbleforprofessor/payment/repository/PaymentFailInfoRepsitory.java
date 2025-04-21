package com.bubble.bubbleforprofessor.payment.repository;

import com.bubble.bubbleforprofessor.payment.entity.PaymentFailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFailInfoRepsitory extends JpaRepository<PaymentFailInfo, Long> {
}
