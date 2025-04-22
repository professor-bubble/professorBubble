package com.bubble.bubbleforprofessor.chatroom.service;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.bubbleforprofessor.user.entity.Professor;

import java.util.UUID;

public interface ChatroomService {
    void createChatroom(Professor professor);
    ChatroomResponseDto findByUserIdAndChatRoomId (UUID userId, int chatRoomId);
}
