package com.bubble.bubbleforprofessor.chatroom.dto;
import com.bubble.bubbleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.bubbleforprofessor.user.dto.UserSimpleResponseDto;
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
    private List<UserSimpleResponseDto> users;
    private List<MessageResponseDto> messages;
}