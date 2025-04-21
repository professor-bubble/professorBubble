package com.bubble.bubbleforprofessor.user.dto.impl;

import com.bubble.bubbleforprofessor.user.dto.OAuth2ResponseDto;

import java.util.Map;

public class GoogleResponseImplDto implements OAuth2ResponseDto {

    private final Map<String, Object> attribute;

    public GoogleResponseImplDto(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
