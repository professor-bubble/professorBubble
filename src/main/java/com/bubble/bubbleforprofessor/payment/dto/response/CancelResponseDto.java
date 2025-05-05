package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelResponseDto extends PaymentResponseDto {
    private String paymentKey;
    private int cancelAmount;
    private String cancelReason;

    @Builder
    public CancelResponseDto(String status, String orderId, String paymentKey, int cancelAmount, String cancelReason) {
        super(status, orderId);
        this.paymentKey = paymentKey;
        this.cancelAmount = cancelAmount;
        this.cancelReason = cancelReason;
    }
}
