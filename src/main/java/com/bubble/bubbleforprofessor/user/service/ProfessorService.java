package com.bubble.bubbleforprofessor.user.service;

import com.bubble.bubbleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.bubbleforprofessor.user.dto.ApprovalRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;
public interface ProfessorService {
    Page<ApprovalRequestDto> getApproveRequests(Pageable pageable);
    void setApprovalStatus(UUID userId);
    void deleteApprovalById(UUID userId);
    void deleteById(UUID userId);
    void createProfessor(UUID userId, ApprovalRequestCreateDto approvalRequestCreateDtoDtoDto);
}
