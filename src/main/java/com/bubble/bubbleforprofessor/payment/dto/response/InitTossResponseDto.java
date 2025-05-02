package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Builder;
import lombok.Getter;

//서버 -> 클라이언트 응답용

@Getter
@Builder
public class InitTossResponseDto {
    private String orderId;
    private int amount;
    private String paymentKey;
}
