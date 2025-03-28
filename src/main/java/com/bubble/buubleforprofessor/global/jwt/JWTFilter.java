package com.bubble.buubleforprofessor.global.jwt;

import com.bubble.buubleforprofessor.user.dto.CustomUserDetails;
import com.bubble.buubleforprofessor.user.entity.Role;
import com.bubble.buubleforprofessor.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization 헤더 추출
        String authorization = request.getHeader("Authorization");

        // Authorization 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 검증 시작
        System.out.println("Authorization now");
        // 토큰 추출("Bearer " 제거)
        String token = authorization.split(" ")[1];

        // 만료시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰에서 username과 role을 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        String userId = jwtUtil.getUserId(token);

        // userEntity 생성
        User user = User.builder()
                .id(UUID.fromString(userId))
                .loginId(username)
                .role(new Role(role))
                .build();

        // UserDetails에 회원정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

