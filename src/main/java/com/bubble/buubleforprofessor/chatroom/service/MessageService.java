package com.bubble.buubleforprofessor.chatroom.service;

import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.entity.Message;

import java.util.UUID;

public interface MessageService {
    Message findByUserIdAndChatroomId(UUID userId, int chatroomId);
    Message save(MessageRequestDto message);
}
