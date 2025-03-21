package com.bubble.buubleforprofessor.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name ="professors")
@Builder
public class Professor {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "professor_id", referencedColumnName = "user_id")
    private User user;

    private String description;

    private int professorNum;

    private boolean isApproved;

    private String department;

    // 승인 여부를 변경하는 메서드
    public void approve() {
        if (!isApproved) {
            this.isApproved = true;
        }
    }

    @Builder
    public Professor(Long id, User user, String description, int professorNum, boolean isApproved, String department) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.professorNum = professorNum;
        this.isApproved = isApproved;
        this.department = department;
    }

}
