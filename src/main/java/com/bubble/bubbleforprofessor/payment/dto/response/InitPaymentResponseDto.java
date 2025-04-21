package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class InitPaymentResponseDto {
    private String orderId;
    private int amount;

    @Builder
    public InitPaymentResponseDto(String orderId, int amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
