package com.bubble.bubbleforprofessor.chatroom.dto;

import com.bubble.bubbleforprofessor.user.dto.ProfessorResponseDto;
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
