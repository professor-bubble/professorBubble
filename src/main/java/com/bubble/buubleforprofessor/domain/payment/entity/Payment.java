package com.bubble.buubleforprofessor.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    //단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
    private Order order;

    //가격
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "payment_key", nullable = false, length = 255)
    private String paymentKey;

    //카카오페, 토스페이 등등 결제 방식
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
}

enum PaymentStatus {
    PENDING,    // 결제 대기
    SUCCEEDED,  // 결제 성공
    FAILED,     // 결제 실패
    CANCELED    // 결제 취소 (환불 등)
}