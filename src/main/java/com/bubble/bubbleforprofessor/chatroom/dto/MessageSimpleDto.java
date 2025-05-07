package com.bubble.bubbleforprofessor.chatroom.dto;


import com.bubble.bubbleforprofessor.chatroom.entity.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageSimpleDto {
    private UUID userId;
    private String userName;
    private Message.MessageType type;
    private String content;
    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime createAt;
    private boolean read=false;

    public void modifyRead(boolean read) {
        this.read = read;
    }

}