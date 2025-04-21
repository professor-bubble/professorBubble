package com.bubble.buubleforprofessor.global.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    public Long getUserIdFromToken(String token) {
        String jwt = token.replace("Bearer ", "");
        //디코딩
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // 비밀 키로 서명 확인 검증
                .parseClaimsJws(jwt)// 서명된 JWT 파싱해서 유효성 체크
                .getBody();
        return Long.parseLong(claims.getSubject()); // JWT의 subject에 user_id 저장 가정
    }
}

//todo
// 로그인 구현기능 merge -> 본 파일 주석 처리 또는 삭제 필요