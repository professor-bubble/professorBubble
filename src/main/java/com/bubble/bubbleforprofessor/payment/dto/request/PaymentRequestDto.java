package com.bubble.bubbleforprofessor.payment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequestDto {
    private String paymentKey;
    private String orderId;
    private int  amount;

    @Builder
    public PaymentRequestDto(String paymentKey, String orderId, int amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

}
