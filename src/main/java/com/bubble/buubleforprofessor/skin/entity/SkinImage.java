package com.bubble.buubleforprofessor.skin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="skin_images")
@Getter
public class SkinImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skin_image_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "skin_id")
    private Skin skin;

    private String imageURL;

    public SkinImage(Skin skin, String imageURL) {
        this.skin = skin;
        this.imageURL = imageURL;
    }
}
