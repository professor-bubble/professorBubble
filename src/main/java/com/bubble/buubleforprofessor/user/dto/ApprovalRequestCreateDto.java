package com.bubble.buubleforprofessor.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ApprovalRequestCreateDto {
    private String description;
    @NotNull(message = "Professor number cannot be null")
    private int professorNum;
    @Size(max = 20, message = "Department name cannot exceed 20 characters")
    private String department;
}
