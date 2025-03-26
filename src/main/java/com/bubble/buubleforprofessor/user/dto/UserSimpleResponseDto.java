package com.bubble.buubleforprofessor.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserSimpleResponseDto {
    private UUID userId;
    private String userName;
}
