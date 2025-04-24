package com.bubble.bubbleforprofessor.global.jwt;

import com.bubble.bubbleforprofessor.auth.service.RefreshTokenService;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public CustomLogoutFilter(RefreshTokenService refreshTokenService, JWTUtil jwtUtil, CookieUtil cookieUtil) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // path and method verify
        String requestUri =request.getRequestURI();
        if (!requestUri.equals("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

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
        try {
            refreshTokenService.removeRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        // 쿠키 삭제
        Cookie cookie = cookieUtil.deleteCookie("refresh");
        response.addCookie(cookie);
    }
}
