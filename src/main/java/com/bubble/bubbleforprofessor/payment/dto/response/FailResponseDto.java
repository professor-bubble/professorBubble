package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FailResponseDto extends PaymentResponseDto {
    private String errorCode;
    private String errorMessage;

    @Builder
    public FailResponseDto(String status, String orderId, String errorCode, String errorMessage){
        super(status, orderId);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
