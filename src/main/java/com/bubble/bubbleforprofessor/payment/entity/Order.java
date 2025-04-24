package com.bubble.bubbleforprofessor.payment.entity;

import com.bubble.bubbleforprofessor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(indexName = "Order")
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY) //user 테이블
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @Column(name="total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name="create_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
    public Order(User user, Integer totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
    }


}

//USER의 주문 활성 및 취소 상태표시
//즉, 유저1이 주문을 넣었다가 취소할 수 있다는 가정하여 ORDER DB에서 주문을 삭제하지않고 아래 ENUM으로 수정
enum OrderStatus {
    PENDING,        //주문 대기
    SUCCEEDED,      //주문 완료
    FAILED,         //주문 실패
    CANCELED        //주문 취소
}