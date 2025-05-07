package com.bubble.bubbleforprofessor.chatroom.service;

import com.bubble.bubbleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageSimpleDto;

import java.util.UUID;

public interface MessageService {
    MessageRequestDto filtering(MessageRequestDto messageRequestDto);

    MessageMongo save(MessageRequestDto message);

    void send(int chatroomId, MessageSimpleDto messageSimpleDto);

    MessageSimpleDto sendAndHandleRead(MessageRequestDto requestDto, boolean isProfessorOnline);

    void markMessagesAsReadByProfessor(Long chatRoomId, UUID professorId);
}