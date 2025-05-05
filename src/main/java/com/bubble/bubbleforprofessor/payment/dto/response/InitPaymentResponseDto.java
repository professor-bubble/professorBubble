package com.bubble.bubbleforprofessor.payment.dto.response;

import com.bubble.bubbleforprofessor.skin.entity.Skin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InitPaymentResponseDto {
    private String orderId;
    private int amount;
    private List<Skin> skins;

    @Builder
    public InitPaymentResponseDto(String orderId, int amount, List<Skin> skins) {
        this.orderId = orderId;
        this.amount = amount;
        this.skins = skins;
    }
}
