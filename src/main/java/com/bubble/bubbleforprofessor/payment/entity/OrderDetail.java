package com.bubble.bubbleforprofessor.payment.entity;

import com.bubble.bubbleforprofessor.skin.entity.Skin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private long orderDetailId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY) //order 테이블
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", referencedColumnName = "skin_id")
    private Skin skin;

    @Builder
    public OrderDetail(Order order, Skin skin, Integer price, Integer quantity){
        this.order = order;
        this.skin = skin;
        this.price = price;
        this.quantity = quantity;
    }



}
