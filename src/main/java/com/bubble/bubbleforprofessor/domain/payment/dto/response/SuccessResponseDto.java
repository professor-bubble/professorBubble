package com.bubble.bubbleforprofessor.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class SuccessResponseDto extends PaymentResponseDto {
    private String paymentKey;
    private int amount;
    private String paymentMethod;

    public SuccessResponseDto(String status, String orderId, String paymentKey, int amount, String paymentMethod) {
        super(status, orderId);
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
}