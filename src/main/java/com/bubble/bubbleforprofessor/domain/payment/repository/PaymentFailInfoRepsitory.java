package com.bubble.bubbleforprofessor.domain.payment.repository;

import com.bubble.bubbleforprofessor.domain.payment.entity.PaymentFailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFailInfoRepsitory extends JpaRepository<PaymentFailInfo, Long> {
}
