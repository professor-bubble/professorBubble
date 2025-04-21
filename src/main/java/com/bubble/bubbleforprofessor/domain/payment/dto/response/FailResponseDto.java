package com.bubble.bubbleforprofessor.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class FailResponseDto extends PaymentResponseDto {
    private String errorCode;
    private String errorMessage;

    public FailResponseDto(String status, String orderId, String errorCode, String errorMessage){
        super(status, orderId);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
