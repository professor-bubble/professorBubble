package com.bubble.buubleforprofessor.global.oauth2;

import com.bubble.buubleforprofessor.global.jwt.JWTUtil;
import com.bubble.buubleforprofessor.user.dto.CustomPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jwt.expirationtime}")
    private Long expirationTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        String username = principal.getUsername();
        String userId = principal.getUserId();

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, userId, expirationTime);

        response.addHeader("Authorization", "Bearer " + token);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:8080/api/users/user");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // js가 접근 못하게 함
//        cookie.setSecure(true); // https 사용

        return cookie;
    }
}
