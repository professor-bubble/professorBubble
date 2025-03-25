package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.UserSimpleDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {
    private long messageId;
    private UserSimpleDto userSimpleDto;
    private LocalDateTime sendTime;
    private String content;
}
