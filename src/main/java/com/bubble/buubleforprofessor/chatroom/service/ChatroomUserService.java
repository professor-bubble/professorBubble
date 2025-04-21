package com.bubble.buubleforprofessor.chatroom.service;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;

import java.util.UUID;

public interface ChatroomUserService {
    void createChatroomUser(UUID userId, int chatroomId, ChatroomEnterRequestDto chatroomEnterRequestDto);
}
