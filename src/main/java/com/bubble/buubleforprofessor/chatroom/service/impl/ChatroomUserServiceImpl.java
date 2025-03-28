package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomUserServiceImpl implements ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;

    @Override
    public void exists(UUID userId, int RoomId) {
        if(!chatroomUserRepository.existsByUserIdAndChatroomId(userId, RoomId)) {
            throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
        }
    }

    @Override
    public ChatroomUser getUserByUserIdAndChatroomId(UUID userId, int chatroomId) {
        ChatroomUser chatroomUser=chatroomUserRepository.findByUserIdAndChatroomId(userId,chatroomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER));
        return chatroomUser;
    }
}
