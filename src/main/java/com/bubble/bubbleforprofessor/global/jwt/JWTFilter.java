package com.bubble.bubbleforprofessor.global.jwt;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;
import com.bubble.bubbleforprofessor.user.entity.Role;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.RoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
    private final RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization 헤더 추출
        String accssToken = request.getHeader("access");

        // Authorization 검증
        if (accssToken == null) {
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 검증 시작
        try {
            jwtUtil.isExpired(accssToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_JWT);
        }

        // 토큰에서 username과 role을 획득
        String username = jwtUtil.getUsername(accssToken);
        Role role = roleRepository.findByName(jwtUtil.getRole(accssToken)).orElseThrow(()-> new CustomException(ErrorCode.NON_EXISTENT_ROLE));
        String userId = jwtUtil.getUserId(accssToken);

        // userEntity 생성
        User user = User.builder()
                .id(UUID.fromString(userId))
                .loginId(username)
                .role(role)
                .build();

        // UserDetails에 회원정보 객체 담기
        CustomPrincipal customPrincipal = new CustomPrincipal(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customPrincipal, null, customPrincipal.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}

