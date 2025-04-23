package com.bubble.bubbleforprofessor.auth.service;

public interface RefreshTokenService {
    public void addRefreshToken(String username, String refresh, Long expiredMs);
    public void updateRefreshToken(String username, String refresh, Long expiredMs);
    public void removeRefreshToken(String refresh);
}
