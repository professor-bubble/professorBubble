package com.bubble.bubbleforprofessor.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    private String id;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
}
