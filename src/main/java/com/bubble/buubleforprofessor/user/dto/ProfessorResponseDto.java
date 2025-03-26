package com.bubble.buubleforprofessor.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProfessorResponseDto {
    private UUID professorId;
    private String professorName;
    private String professorImageUrl;
}
