package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.ProfessorDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChatroomDto {
    private int chatroomId;
    private ProfessorDto professorDto;
    private LocalDateTime createdAt;
    private List<UserSimpleDto> users;
    private List<MessageDto> messages;
}
