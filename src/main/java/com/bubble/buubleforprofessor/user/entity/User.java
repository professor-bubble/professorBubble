package com.bubble.buubleforprofessor.user.entity;

import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.converter.UUIDConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID 자동 생성 전략 적용
    @Convert(converter = UUIDConverter.class)  // UUID ↔ BINARY(16) 변환 적용
    @Column(name="user_id",columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String phoneNumber;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private University university;

}
