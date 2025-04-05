package com.bubble.buubleforprofessor.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequestDto {
    String loginId;
    String password;
    String userName;
}