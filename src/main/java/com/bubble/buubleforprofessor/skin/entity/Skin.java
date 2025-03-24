package com.bubble.buubleforprofessor.skin.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private boolean isDelete=false;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    public void delete()
    {
        isDelete=true;
    }
    public void active()
    {
        isDelete=false;
    }
    @Builder
    public Skin(int id, String name, int price, String description, boolean isDelete, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isDelete = isDelete;
        this.category = category;
    }
}
