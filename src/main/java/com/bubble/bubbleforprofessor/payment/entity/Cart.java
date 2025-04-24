package com.bubble.bubbleforprofessor.payment.entity;

import com.bubble.bubbleforprofessor.skin.entity.Skin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bubble.bubbleforprofessor.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skin skin;

    @Column(name="pending_quantity")//확정되지 않은 수량, 추후 orderdetail에서 확정된 수량 표시
    private int pendingQuantity;

    public void updateQuantity(int pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }
}
