package com.bubble.bubbleforprofessor.chatroom.service;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomDetailResponseDto;
import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.user.entity.Professor;

import java.util.List;
import java.util.UUID;

public interface ChatroomService {
    Chatroom createChatroom(Professor professor);
    ChatroomDetailResponseDto findByUserIdAndChatRoomId (UUID userId, int chatRoomId);
    List<ChatroomResponseDto> findAllChatroomByUserId(UUID userId);
}
