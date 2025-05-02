package com.bubble.bubbleforprofessor.payment.entity;

import com.bubble.bubbleforprofessor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Orders")
@Where(clause = "is_deleted = false") // 소프트 삭제된 데이터 제외
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY) //user 테이블
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name="total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //주문 생성시 자동으로 값 저장 save()
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<OrderDetail> orderDetails = new ArrayList<>();


    public void updateOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void setTotalAmount(int totalAmount){
        this.totalAmount = totalAmount;
    }

    @Builder
    public Order(Long orderId, User user, Integer totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}

