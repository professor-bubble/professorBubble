package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomService;
import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.service.SkinService;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;

import com.bubble.buubleforprofessor.user.service.ProfessorService;
import jakarta.validation.Valid;
import com.bubble.buubleforprofessor.user.dto.CustomUserDetails;
import com.bubble.buubleforprofessor.user.dto.JoinRequestDto;
import com.bubble.buubleforprofessor.user.dto.LoginRequestDto;
import com.bubble.buubleforprofessor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final ProfessorService professorService;
    private final SkinService skinService;
    private final ChatroomService chatroomService;
    private final UserService userService;

    @GetMapping("/user")
    public String mainPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "user controller - " + userDetails.getUserId() + " - " + userDetails.getPassword() + " - " + userDetails.getRole();
    }

    @PostMapping("/join")
    public String join(JoinRequestDto joinRequestDto) {

        return userService.createUser(joinRequestDto);
    }


    //교수 승인 요청. 교수데이터생성
    @PostMapping("/{userId}/approve-request")
    public ResponseEntity<Void> approveRequest(@PathVariable("userId") UUID userId,
                                               @Valid @RequestBody ApprovalRequestCreateDto approvalRequestCreateDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        professorService.createProfessor(userId, approvalRequestCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{userId}/skin")
    public ResponseEntity<Page<SkinResponseDto>> findSkinsByUserId(@PathVariable(value = "userId",required = true)UUID userId,
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

    //스킨 적용 여부 변경
    @PatchMapping(value = "/{userId}/skin/{skinId}",produces = "application/json")
    public ResponseEntity<Boolean> modifySkinStatus(@PathVariable(required = true) UUID userId,
                                                    @PathVariable(required = true) int skinId)
    {
        skinService.modifySkinStatus(userId,skinId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{userId}/chatroom/{chatroomId}")
    public ResponseEntity<ChatroomResponseDto> getChatroom(@PathVariable UUID userId,
                                                           @PathVariable int chatroomId) {
        ChatroomResponseDto chatroomDto = chatroomService.findByUserIdAndChatRoomId(userId,chatroomId);
        return ResponseEntity.ok(chatroomDto);
    }
}
