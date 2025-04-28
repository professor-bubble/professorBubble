package com.bubble.bubbleforprofessor.payment.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequestDto {
  private List<OrderDetailRequestDto> items;

  @Builder
  public OrderRequestDto(List<OrderDetailRequestDto> items) {
      this.items = items;
  }
}
