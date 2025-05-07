package com.bubble.bubbleforprofessor.chatroom.service;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;

import java.util.UUID;

public interface ChatroomUserService {
    void exists(UUID userId, int RoomId);
    ChatroomUser getUserByUserIdAndChatroomId(UUID userId, int chatroomId);
    void createChatroomUser(UUID userId, int chatroomId, ChatroomEnterRequestDto chatroomEnterRequestDto);

}
