package com.bubble.buubleforprofessor.auth.service;

public interface RefreshTokenService {
    public void addRefreshToken(String username, String refresh, Long expiredMs);
    public void updateRefreshToken(String username, String refresh, Long expiredMs);
}
