package com.bubble.buubleforprofessor.domain.university.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name="University")
@Builder
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="university_id")
    private int universityId;

    @Column(name="university_name", nullable = false)
    private String universityName;
}
