package com.bubble.buubleforprofessor.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ApprovalRequestDto {
    private UUID userId;
    private String universityName;
    private String department;
    private int professorNum;
    private String professorName;
    private String phoneNumber;
    private String email;

}
