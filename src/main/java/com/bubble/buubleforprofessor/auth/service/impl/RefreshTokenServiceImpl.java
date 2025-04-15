package com.bubble.buubleforprofessor.auth.service.impl;

import com.bubble.buubleforprofessor.auth.entity.RefreshToken;
import com.bubble.buubleforprofessor.auth.repository.RefreshTokenRepository;
import com.bubble.buubleforprofessor.auth.service.RefreshTokenService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void addRefreshToken(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis()+ expiredMs);

        RefreshToken refreshEntity = new RefreshToken(username, refresh, date.toString());

        refreshTokenRepository.save(refreshEntity);
    }

    @Transactional
    public void updateRefreshToken(String username, String refresh, Long expiredMs) {
        Optional<RefreshToken> optionalRefresh = refreshTokenRepository.findByRefreshToken(refresh);

        if (optionalRefresh.isEmpty()) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        Date date = new Date(System.currentTimeMillis()+ expiredMs);

        RefreshToken refreshToken = optionalRefresh.get().updateRefreshToken(refresh, date.toString());
    }
}
