package com.bubble.buubleforprofessor.user.dto;

public interface OAuth2ResponseDto {
    // 제공자
    String getProvider();
    // 제공자에서 발급하는 아이디
    String getProviderId();
    // 이메일
    String getEmail();
    // 이름
    String getName();
}
