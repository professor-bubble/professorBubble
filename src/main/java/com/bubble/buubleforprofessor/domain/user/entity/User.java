package com.bubble.buubleforprofessor.domain.user.entity;

import com.bubble.buubleforprofessor.domain.payment.entity.Order;
import com.bubble.buubleforprofessor.domain.user.dto.JoinRequestDto;
import com.bubble.buubleforprofessor.domain.user.converter.UUIDConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private Role role;

    public void modifyRole(Role role) {
        this.role = role;
    }

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Professor professor;

    public void modifyProfessor(Professor professor) {
        this.professor = professor;
    }

    //user삭제 시 order보존을 위해 cascade에서(PERSIST : 추가, MERGE : 수정만 허용) remove 제외 & 고아값 제거 방지
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private List<Order> orders = new ArrayList<>();


    // 🔥 모든 필드를 받는 생성자를 추가 (Builder를 사용하려면 필요)
    @Builder
    public User(UUID id, String loginId, String password,Timestamp createdAt
            ,Timestamp lastLoginAt,String name, String phoneNumber,
                String email,Role role) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        //this.university = university;
        this.role = role;
    }

    public User userFromDto(JoinRequestDto joinRequestDto) {
        this.loginId = joinRequestDto.getLoginId();
        this.password = joinRequestDto.getPassword();
        this.createdAt = java.sql.Timestamp.valueOf(LocalDateTime.now());
        this.lastLoginAt = java.sql.Timestamp.valueOf(LocalDateTime.now());
        this.name = "nameTest";
        this.phoneNumber = "010-1234-1234";
        this.email = "email@email.com";
      //  this.university = null;
        this.role = null;

        return this;
    }

}