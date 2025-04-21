package com.bubble.bubbleforprofessor.chatroom.service;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;

import java.util.UUID;

public interface ChatroomUserService {
    void createChatroomUser(UUID userId, int chatroomId, ChatroomEnterRequestDto chatroomEnterRequestDto);
}
