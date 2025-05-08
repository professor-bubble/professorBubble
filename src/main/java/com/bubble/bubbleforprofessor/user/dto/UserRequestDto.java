package com.bubble.bubbleforprofessor.user.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String id;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
}
