package com.bubble.bubbleforprofessor.chatroom.dto;

import com.bubble.bubbleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.bubbleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatroomDetailResponseDto {
    private int chatroomId;

    private LocalDateTime createTime;

    private Timestamp lastSeenAt;

    private ProfessorResponseDto professor;
}