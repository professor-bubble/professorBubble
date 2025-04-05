package com.bubble.buubleforprofessor.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id" )
    private int id;

    @Column(length = 20,unique = true , nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}