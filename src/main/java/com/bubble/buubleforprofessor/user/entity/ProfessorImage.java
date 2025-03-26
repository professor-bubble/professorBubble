package com.bubble.buubleforprofessor.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "professor_images")
public class ProfessorImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professor_image_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "professor_id",referencedColumnName = "professor_id")
    private Professor professor;

    private String url;

    @Builder
    public ProfessorImage(Professor professor, String url) {
        this.professor = professor;
        this.url = url;
    }
}
