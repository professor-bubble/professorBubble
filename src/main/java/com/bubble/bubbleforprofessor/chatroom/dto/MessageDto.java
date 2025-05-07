package com.bubble.bubbleforprofessor.chatroom.dto;


import com.bubble.bubbleforprofessor.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {
    private long messageId;
    private UserSimpleResponseDto sendUser;
    private LocalDateTime sendTime;
    private String content;
}