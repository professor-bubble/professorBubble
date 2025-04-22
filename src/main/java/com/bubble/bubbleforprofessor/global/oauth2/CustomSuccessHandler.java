package com.bubble.bubbleforprofessor.global.oauth2;

import com.bubble.bubbleforprofessor.auth.service.RefreshTokenService;
import com.bubble.bubbleforprofessor.global.jwt.CookieUtil;
import com.bubble.bubbleforprofessor.global.jwt.JWTUtil;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        String username = principal.getUsername();
        String userId = principal.getUserId();

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 발행
        String accessToken = jwtUtil.createAccessToken(username, role, userId);
        String refreshToken = jwtUtil.createRefreshToken(username, role, userId);

        // 토큰 db 저장
        refreshTokenService.addRefreshToken(username, refreshToken, jwtUtil.getRefreshExpiration());

        // 응답 설정
        response.addHeader("access", accessToken);
        response.addCookie(cookieUtil.createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }
}
