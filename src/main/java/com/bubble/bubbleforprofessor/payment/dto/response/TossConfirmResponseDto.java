package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TossConfirmResponseDto {
    private String paymentKey;
    private String orderId;
    private int totalAmount;
    private String status;
    private String method;
    private String approvedAt;
}
