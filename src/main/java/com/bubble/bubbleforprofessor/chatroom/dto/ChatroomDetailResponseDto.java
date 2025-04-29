package com.bubble.bubbleforprofessor.chatroom.dto;

import com.bubble.bubbleforprofessor.user.dto.ProfessorResponseDto;
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
