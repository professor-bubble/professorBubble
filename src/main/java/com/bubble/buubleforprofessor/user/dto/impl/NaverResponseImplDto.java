package com.bubble.buubleforprofessor.user.dto.impl;

import com.bubble.buubleforprofessor.user.dto.OAuth2ResponseDto;

import java.util.Map;

public class NaverResponseImplDto implements OAuth2ResponseDto {

    private final Map<String, Object> attributes;

    public NaverResponseImplDto(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
