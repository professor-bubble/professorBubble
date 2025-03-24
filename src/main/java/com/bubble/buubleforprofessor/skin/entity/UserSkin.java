package com.bubble.buubleforprofessor.skin.entity;

import com.bubble.buubleforprofessor.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="user_skins")
public class UserSkin {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name="user_skin_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Skin skin;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private boolean active=false;

    @Builder
    public UserSkin(Skin skin, User user, boolean active) {
        this.skin = skin;
        this.user = user;
        this.active = active;
    }
}
