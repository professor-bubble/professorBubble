package com.bubble.bubbleforprofessor.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.expirationtime")
@Setter
@Getter
public class JWTExpirationProperties {
    private Long access;
    private Long refresh;
}
