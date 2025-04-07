package com.bubble.buubleforprofessor.domain.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {
    protected String status;
    protected String orderId;
    protected LocalDateTime timestamp;

    public PaymentResponseDto(String status, String orderId) {
        this.status = status;
        this.orderId = orderId;
        this.timestamp = LocalDateTime.now();
    }
}
