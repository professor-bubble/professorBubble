package com.bubble.buubleforprofessor.chatroom.service;

import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;

import java.util.UUID;

public interface ChatroomUserService {
    void exists(UUID userId, int RoomId);
    ChatroomUser getUserByUserIdAndChatroomId(UUID userId, int chatroomId);
}
