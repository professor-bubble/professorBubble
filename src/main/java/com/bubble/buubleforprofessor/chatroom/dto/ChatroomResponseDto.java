package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatroomResponseDto {

    private int chatroomId;

    private LocalDateTime createTime;

    private Timestamp lastSeenAt;

    private ProfessorResponseDto professor;
}
