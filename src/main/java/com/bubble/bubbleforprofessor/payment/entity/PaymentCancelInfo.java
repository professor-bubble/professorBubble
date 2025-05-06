package com.bubble.bubbleforprofessor.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PaymentCancelInfo")
public class PaymentCancelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancel_id")
    private Long cancelId;

    @Column(name ="cancel_reason")
    private String cancelReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, referencedColumnName = "payment_id")
    private Payment payment;

    @Builder
    public PaymentCancelInfo(String cancelReason, Payment payment) {
        this.cancelReason = cancelReason;
        this.payment = payment;
    }
}
