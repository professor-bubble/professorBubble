package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import com.bubble.buubleforprofessor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/sub-admin")
@PreAuthorize("hasRole('SUB_ADMIN')")
@RequiredArgsConstructor
public class SubAdminController {

    public final ProfessorService professorService;
    public final UserService userService;

    //컨트롤러에서는 디코딩된 userId 값이 들어오는거.
    @GetMapping("/approve-requests")
    public ResponseEntity<List<ApprovalRequestDto>> approveRequests(
            @RequestHeader(value = "X-USER-ID",required = true) UUID userId) {
        if(userId == null || userId.toString().isBlank()) {
            throw new CustomException(ErrorCode.INVALID_USERID);
        }
        if(!userService.isExist(userId))
        {
            throw new CustomException(ErrorCode.INVALID_USERID);
        }

        List<ApprovalRequestDto> requests = professorService.getApproveRequests();
        if(requests == null || requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }



}
