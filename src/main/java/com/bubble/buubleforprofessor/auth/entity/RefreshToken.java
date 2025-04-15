package com.bubble.buubleforprofessor.auth.entity;

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
    private String refreshToken;
    private String expiration;

    public RefreshToken(String username, String refreshToken, String expiration) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public RefreshToken updateRefreshToken(String refreshToken, String expiration) {
        this.refreshToken = refreshToken;
        this.expiration = expiration;

        return this;
    }
}
