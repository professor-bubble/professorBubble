package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageRequestDto {
    private int chatRoomId;
    private UUID userId;
    private String userName;
    private String content;
}
