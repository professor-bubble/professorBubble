package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final ProfessorService professorService;

    //교수 승인 요청. 교수데이터생성
    @PostMapping("/{userId}/approve-request")
    public ResponseEntity<Void> approveRequest(@RequestHeader(value = "X-USER-ID") UUID jwtUserId,
                                               @PathVariable("userId") UUID userId,
                                               @Valid @RequestBody ApprovalRequestCreateDto approvalRequestCreateDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        professorService.createProfessor(userId, approvalRequestCreateDto);
        return ResponseEntity.ok().build();
    }
}
