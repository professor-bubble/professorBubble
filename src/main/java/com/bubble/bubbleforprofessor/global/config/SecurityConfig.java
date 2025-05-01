package com.bubble.bubbleforprofessor.global.config;

import com.bubble.bubbleforprofessor.auth.service.RefreshTokenService;
import com.bubble.bubbleforprofessor.global.jwt.*;
import com.bubble.bubbleforprofessor.global.oauth2.CustomSuccessHandler;
import com.bubble.bubbleforprofessor.user.repository.RoleRepository;
import com.bubble.bubbleforprofessor.user.service.impl.CustomOAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserServiceImpl customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        String hierarchy = """
              ROLE_ADMIN > ROLE_UNIVERSITY_ADMIN
              ROLE_ADMIN > ROLE_STUDENT
              ROLE_ADMIN > ROLE_PROFESSOR
              """;

        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RefreshTokenService refreshTokenService) throws Exception {

        http
                .csrf(auth -> auth.disable());
        http
                .formLogin(auth -> auth.disable());
        http
                .httpBasic(auth -> auth.disable());

        // 경로별 인가작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/token", "/api/users", "/").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                );

        // JWT 필터 등록
        http
//                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
                .addFilterAfter(new JWTFilter(jwtUtil, roleRepository), OAuth2LoginAuthenticationFilter.class);

        // loginFilter 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil, refreshTokenService), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 설정
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                        .failureUrl("/login?error")
                );

        // logout 설정
        http
                .logout(auth -> auth.disable());
        http
                .addFilterBefore(new CustomLogoutFilter(refreshTokenService, jwtUtil, cookieUtil), LogoutFilter.class);

        // h2 console
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}
