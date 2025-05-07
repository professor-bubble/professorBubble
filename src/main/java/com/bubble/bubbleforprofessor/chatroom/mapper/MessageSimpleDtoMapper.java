package com.bubble.bubbleforprofessor.chatroom.mapper;

import com.bubble.bubbleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageSimpleDto;

public class MessageSimpleDtoMapper {
    public static MessageSimpleDto toDto(MessageMongo message, boolean isRead) {
        return MessageSimpleDto.builder()
                .userId(message.getUserId())
                .userName(message.getUserName())
                .type(message.getMessageType())
                .content(message.getContent())
                .createAt(message.getSendTime())
                .read(isRead)
                .build();
    }
}
