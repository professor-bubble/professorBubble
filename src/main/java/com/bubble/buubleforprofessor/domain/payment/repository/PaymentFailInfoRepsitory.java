package com.bubble.buubleforprofessor.domain.payment.repository;

import com.bubble.buubleforprofessor.domain.payment.entity.PaymentFailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFailInfoRepsitory extends JpaRepository<PaymentFailInfo, Long> {
}
