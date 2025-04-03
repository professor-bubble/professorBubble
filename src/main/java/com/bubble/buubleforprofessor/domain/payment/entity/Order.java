package com.bubble.buubleforprofessor.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name="total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name="create_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //주문 생성시 자동으로 값 저장 save()
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
