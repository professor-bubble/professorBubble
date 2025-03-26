package com.bubble.buubleforprofessor.chatroom.service;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.user.entity.Professor;

import java.util.UUID;

public interface ChatroomService {
    void createChatroom(Professor professor);
    ChatroomResponseDto findByUserIdAndChatRoomId (UUID userId, int chatRoomId);
}
