package com.bubble.buubleforprofessor.user.entity;

import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.converter.UUIDConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 JPA를 위한 기본 생성자 추가
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID 자동 생성 전략 적용
    @Convert(converter = UUIDConverter.class)  // UUID ↔ BINARY(16) 변환 적용
    @Column(name="user_id",columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;
    @Column(length = 50, nullable = false,unique = true)
    private String loginId;
    @Column( nullable = false)
    private String password;
    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = true)
    private Timestamp lastLoginAt;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 15, nullable = false,unique = true)
    private String phoneNumber;
    @Column(length = 50, nullable = false,unique = true)
    private String email;

    @ManyToOne
    private University university;

    @ManyToOne
    private Role role;

    public void modifyRole(Role role) {
        this.role = role;
    }

    // 🔥 모든 필드를 받는 생성자를 추가 (Builder를 사용하려면 필요)
    @Builder
    public User(UUID id, String loginId, String password,Timestamp createdAt
            ,Timestamp lastLoginAt,String name, String phoneNumber,
                String email, University university,Role role) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.university = university;
        this.role = role;
    }

}
