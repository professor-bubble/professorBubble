package com.bubble.buubleforprofessor.user.service;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.UUID;
public interface ProfessorService {
    Page<ApprovalRequestDto> getApproveRequests(Pageable pageable);
    void setApprovalStatus(UUID userId);
    void deleteApprovalById(UUID userId);
    void deleteById(UUID userId);
    void createProfessor(UUID userId, ApprovalRequestCreateDto approvalRequestCreateDtoDtoDto);
}
