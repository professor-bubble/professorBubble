package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageSimpleDto {
    private String userName;
    private Message.MessageType type;
    private String content;
    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime createAt;
}
