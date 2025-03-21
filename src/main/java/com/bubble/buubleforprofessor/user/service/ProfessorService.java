package com.bubble.buubleforprofessor.user.service;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;

import java.util.List;

public interface ProfessorService {
    List<ApprovalRequestDto> getApproveRequests();
}
