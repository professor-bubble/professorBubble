package com.bubble.buubleforprofessor.skin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name= "skins")
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skin_id")
    private int id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(name="is_delete",nullable = false)
    private boolean isDelete=false;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName = "category_id", nullable = false)
    private Category category;

    public void delete()
    {
        isDelete=true;
    }
    public void active()
    {
        isDelete=false;
    }

    @OneToMany(mappedBy = "skin", fetch = FetchType.LAZY)
    private List<SkinImage> skinImages;

    public void modifySkinImages(List<SkinImage> skinImages)
    {
        this.skinImages=skinImages;
    }

    @Builder
    public Skin(String name, int price, String description, boolean isDelete, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isDelete = isDelete;
        this.category = category;
    }
}
