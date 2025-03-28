package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatroomResponseDto {
    private int chatroomId;
    private ProfessorResponseDto professorDto;
    private LocalDateTime createdAt;
    private List<UserSimpleResponseDto> users;
    private List<MessageResponseDto> messages;
}
