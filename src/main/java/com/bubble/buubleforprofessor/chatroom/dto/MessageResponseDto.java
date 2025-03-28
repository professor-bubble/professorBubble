package com.bubble.buubleforprofessor.chatroom.dto;

import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponseDto {
    private long messageId;
    private UserSimpleResponseDto sendUser;
    private LocalDateTime sendTime;
    private String content;
}
