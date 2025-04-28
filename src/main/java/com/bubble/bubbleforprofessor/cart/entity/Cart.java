package com.bubble.bubbleforprofessor.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bubble.bubbleforprofessor.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    //단방향 (cart -> user)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    // 양방향
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "cart_created_at", nullable = false)
    private LocalDateTime cartCreatedAt = LocalDateTime.now();

    @Column(name = "cart_update_at", nullable = false)
    private LocalDateTime cartUpdateAt = LocalDateTime.now();


    // 장바구니 생성시 어느 유저인지만 저장하면됨
    @Builder
    public Cart(User user) {
        this.user = user;
    }

    // 장바구니에 아이템 추가 시
    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this); // 양방향 연결 유지
    }


    // 장바구니 초기화
    public void clearItems() {
        this.items.clear();
    }

}
