package com.bubble.buubleforprofessor.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name ="professors")
public class Professor {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "professor_id", referencedColumnName = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT",nullable = true)
    private String description;

    @Column(nullable = false)
    private int professorNum;
    @Column(nullable = false)
    private boolean isApproved=false;
    @Column(length = 20, nullable = false)
    private String department;

    @OneToOne(mappedBy = "professor")
    private ProfessorImage professorImage;

    public void modifyProfessorImage(ProfessorImage professorImage) {
        this.professorImage = professorImage;
    }

    // 승인 여부를 변경하는 메서드
    public void approve() {
        if (!isApproved) {
            this.isApproved = true;
        }
    }

    @Builder
    public Professor(User user, String description, int professorNum, boolean isApproved, String department) {
        this.user = user;
        this.description = description;
        this.professorNum = professorNum;
        this.isApproved = isApproved;
        this.department = department;
    }

}
