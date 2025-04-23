package com.bubble.bubbleforprofessor.global.jwt;

import com.bubble.bubbleforprofessor.auth.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public LogoutHandler(RefreshTokenService refreshTokenService, JWTUtil jwtUtil, CookieUtil cookieUtil) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 쿠키 받기
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        // 필터 넘기기
        if (refreshToken == null || jwtUtil.getCategory(refreshToken).equals("refresh")) {
            return;
        }

        // DB 삭제
        refreshTokenService.removeRefreshToken(refreshToken);

        // 쿠키 삭제
        Cookie cookie = cookieUtil.deleteCookie("refresh");
        response.addCookie(cookie);
    }
}
