package com.bubble.bubbleforprofessor.payment.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderDetailRequestDto {
    private Integer skinId;
    private Integer quantity;

    @Builder
    public OrderDetailRequestDto(Integer skinId, Integer quantity) {
        this.skinId = skinId;
        this.quantity = quantity;
    }
}
