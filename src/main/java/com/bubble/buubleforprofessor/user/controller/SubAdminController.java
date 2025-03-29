package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/sub-admin")
@PreAuthorize("hasRole('SUB_ADMIN')")
@RequiredArgsConstructor
public class SubAdminController {

    public final ProfessorService professorService;

    //컨트롤러에서는 디코딩된 userId 값이 들어오는거.
    //교수 승인요청 리스트를 모두 봄
    //todo 페이징 처리로 바꿀것
    @GetMapping("/approve-requests")
    public ResponseEntity<Page<ApprovalRequestDto>> approveRequests(
            @RequestParam int pageNum) {

        if(pageNum<0)
        {
            pageNum=0;
        }
        PageRequest pageable= PageRequest.of(pageNum, 10);
        Page<ApprovalRequestDto> requests = professorService.getApproveRequests(pageable);
        if(requests == null || requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }
    //교수 승인 요청을 수락
    @PatchMapping(value = "/{userId}/approve", produces = "application/json")
    public ResponseEntity<Boolean> approveRequest(@PathVariable UUID userId) {

       professorService.setApprovalStatus(userId);

       return ResponseEntity.ok(true);
    }
    //교수 승인요청을 거절
    @DeleteMapping(value = "/{userId}/approve" , produces = "application/json")
    public ResponseEntity<Boolean> deleteApproveRequest(@PathVariable UUID userId) {
        professorService.deleteApprovalById(userId);
        return ResponseEntity.ok(true);
    }

    // 교수를 일반유저로 변경. 사용안하지만 일단 구현해둠.
    @DeleteMapping(value = "/{userId}/professor" , produces = "application/json")
    public ResponseEntity<Boolean> DeleteProfessor(@PathVariable UUID userId) {
        professorService.deleteById(userId);
        return ResponseEntity.ok(true);
    }

}
