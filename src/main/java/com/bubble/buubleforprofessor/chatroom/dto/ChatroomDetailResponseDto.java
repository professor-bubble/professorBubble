package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatroomDetailResponseDto {
    private int chatroomId;
    private ProfessorResponseDto professorDto;
    private LocalDateTime createdAt;
    private long users;
    private List<MessageDto> messages;
}
