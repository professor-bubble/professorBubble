package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.entity.Skin;
import com.bubble.buubleforprofessor.skin.service.SkinService;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

    private final ProfessorService professorService;
    private final SkinService skinService;

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

    @GetMapping("{userId}/skin")
    public ResponseEntity<Page<SkinResponseDto>> findSkinsByUserId(@RequestHeader(value = "X-USER-ID",required = true) UUID jwtUserId,
                                                        @PathVariable(value = "userId",required = true)UUID userId,
                                                        @RequestParam(value = "pageNum",defaultValue = "0") int pageNum)
    {
        PageRequest pageRequest=PageRequest.of(pageNum, 10);
        Page<SkinResponseDto> skins= skinService.getSkinsByUserId(userId, pageRequest);
        if(skins==null || skins.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(skins);
    }
}
