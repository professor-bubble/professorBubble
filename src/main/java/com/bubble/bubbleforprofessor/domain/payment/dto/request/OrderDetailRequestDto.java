package com.bubble.bubbleforprofessor.domain.payment.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OrderDetailRequestDto {
    private Long skinId;
    private Integer quantity;

    @Builder
    public OrderDetailRequestDto(Long skinId, Integer quantity) {
        this.skinId = skinId;
        this.quantity = quantity;
    }
}
