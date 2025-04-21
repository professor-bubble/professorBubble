package com.bubble.bubbleforprofessor.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "PaymentFailInfo")
public class PaymentFailInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fail_id")
    private Long failId;

    @Column(name = "fail_message")
    private String failMessage;

    @Column(name = "fail_code")
    private String failCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="payment_id", nullable = false , referencedColumnName = "payment_id")
    private Payment payment;
}
