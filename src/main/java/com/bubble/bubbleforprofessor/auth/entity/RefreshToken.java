package com.bubble.bubbleforprofessor.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    @Column(length = 512)
    private String refresh;
    private String expiration;

    public RefreshToken(String username, String refresh, String expiration) {
        this.username = username;
        this.refresh = refresh;
        this.expiration = expiration;
    }

    public void updateRefreshToken(String refresh, String expiration) {
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
