package com.bubble.bubbleforprofessor.auth.repository;

import com.bubble.bubbleforprofessor.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefresh(String refreshToken);
}
