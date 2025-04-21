package com.bubble.bubbleforprofessor.global.config;

import com.bubble.bubbleforprofessor.global.jwt.JWTFilter;
import com.bubble.bubbleforprofessor.global.jwt.JWTUtil;
import com.bubble.bubbleforprofessor.global.jwt.LoginFilter;
import com.bubble.bubbleforprofessor.global.oauth2.CustomSuccessHandler;
import com.bubble.bubbleforprofessor.user.service.impl.CustomOAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserServiceImpl customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    @Value("${jwt.expirationtime}")
    private Long expirationTime;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(auth -> auth.disable());
        http
                .formLogin(auth -> auth.disable());
        http
                .httpBasic(auth -> auth.disable());

        // 경로별 인가작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/token", "/api/users", "/","/**").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                );

//        // JWT 필터 등록
//        http
//                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        // loginFilter 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, expirationTime), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 설정
//        http
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
//                                .userService(customOAuth2UserService))
//                        .successHandler(customSuccessHandler)
//                        .failureUrl("/login?error")
//                );

        // h2 console
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}
