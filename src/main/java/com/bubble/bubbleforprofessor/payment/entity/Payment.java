package com.bubble.bubbleforprofessor.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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


    public void updatePaymentStatus(String status, String approvedAt) {
        this.paymentStatus = status;

        if (approvedAt != null && ("DONE".equals(status) || "FAILED".equals(status) || "CANCELED".equals(status))) {
            /* 토스페이먼츠에서 제공하는 approvedAt은 시간대를포한한 ISO 8601 형식(날짜 + 시간 + 시간대)
            * 정확한 시간 변환을 위해 OffseDateTime으로 먼저 파싱 필요
            * */
            OffsetDateTime odt = OffsetDateTime.parse(approvedAt);
            this.paymentTime = odt.toLocalDateTime(); // LocalDateTime으로 변환해서 저장
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
