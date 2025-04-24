package com.bubble.bubbleforprofessor.payment.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequestDto {
    private List<Long> cartIds;

    @Builder
    public OrderRequestDto(List<Long> cartIds) {
        this.cartIds = cartIds;
    }


/*    <기존 방식>
    private List<OrderDetailRequestDto> itmes;

//todo 토스페이먼츠 결제 요청시 orderId, amount(=totalPrice) 필수
//클라이언트로 넘경서 , 클라이언트가 이를 토스페이먼츠 결제 창에전달

    @Builder
    public OrderRequestDto(List<OrderDetailRequestDto> itmes) {
        this.itmes = itmes;
    }

 */
}
