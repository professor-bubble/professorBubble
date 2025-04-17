package com.bubble.buubleforprofessor.chatroom.service;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.buubleforprofessor.chatroom.entity.Message;

import java.util.UUID;

public interface MessageService {
    MessageRequestDto filtering(MessageRequestDto messageRequestDto);
    MessageMongo save(MessageRequestDto message);
    void send(int chatroomId,MessageSimpleDto messageSimpleDto);
}
