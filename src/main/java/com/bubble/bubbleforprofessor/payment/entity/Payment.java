package com.bubble.bubbleforprofessor.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    //카카오페이, 토스페이 등등 결제 방식
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING";

    public void updatePaymentStatus(String status) {
        this.paymentStatus = status;
        if ("DONE".equals(status) || "FAILED".equals(status)) {
            this.paymentTime = LocalDateTime.now();
        }else if ("CANCELED".equals(status)) {
            this.paymentTime = LocalDateTime.now();
        }
    }

    @Builder
    public Payment(Order order, Integer amount, String paymentKey, String paymentMethod) {
        this.order = order;
        this.amount = amount;
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
    }
}
