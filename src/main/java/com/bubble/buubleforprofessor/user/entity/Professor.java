package com.bubble.buubleforprofessor.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name ="professors")
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

}
