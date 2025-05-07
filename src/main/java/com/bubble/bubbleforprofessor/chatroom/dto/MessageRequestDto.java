package com.bubble.bubbleforprofessor.chatroom.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageRequestDto {
    private int chatRoomId;
    private UUID userId;
    private String userName;
    private String content;
}