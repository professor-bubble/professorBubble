package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomUserServiceImpl implements ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    @Override
    public void createChatroomUser(UUID userId, int chatroomId, ChatroomEnterRequestDto chatroomEnterRequestDto) {
        if(chatroomUserRepository.existsByUserIdAndChatroomId(userId, chatroomId)) {
           throw new CustomException(ErrorCode.EXISTENT_CHATROOM_USER);
        }
        User user= userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_USER));
        Chatroom chatroom=chatroomRepository.findById(chatroomId).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_CHATROOM));
        String nickName= chatroomEnterRequestDto.getNickName();
        ChatroomUser chatroomUser = new ChatroomUser(chatroom,user,nickName);
        chatroomUserRepository.save(chatroomUser);
    }
}
