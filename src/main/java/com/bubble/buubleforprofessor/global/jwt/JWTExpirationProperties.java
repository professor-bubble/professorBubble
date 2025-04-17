package com.bubble.buubleforprofessor.global.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.expirationtime")
@Getter
public class JWTExpirationProperties {
    public Long access;
    public Long refresh;
}
