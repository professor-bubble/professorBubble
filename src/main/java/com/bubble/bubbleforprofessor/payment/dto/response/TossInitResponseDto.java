package com.bubble.bubbleforprofessor.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

//Toss API 응답용

@Getter
@NoArgsConstructor
public class TossInitResponseDto {
    private String paymentKey;
    private String checkoutUrl;   // 클라이언트가 결제 화면으로 이동할 URL

    public TossInitResponseDto(String paymentKey, String checkoutUrl) {
        this.paymentKey = paymentKey;
        this.checkoutUrl = checkoutUrl;
    }
}

