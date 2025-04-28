package com.bubble.bubbleforprofessor.cart.entity;

import com.bubble.bubbleforprofessor.skin.entity.Skin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cartItem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    //단방향 (CartItem → Cart)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // 단방향 (cartItem -> skin)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin skin;

    @Column(name = "temp_quantity", nullable = false)
    private int tempQuantity;


    @Builder
    public CartItem(Skin skin, int tempQuantity) {
        this.skin = skin;
        this.tempQuantity = tempQuantity;
    }

    //어느 카트에 담길지 설정
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void updateQuantity(int newQuantity) {
        this.tempQuantity = newQuantity;
    }
}
