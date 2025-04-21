package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.bubbleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomUserServiceImpl implements ChatroomUserService {

    private final ChatroomUserRepository chatroomUserRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ProfessorRepository professorRepository;
    @Override
    public void createChatroomUser(UUID userId, int chatroomId, ChatroomEnterRequestDto chatroomEnterRequestDto) {
        if(chatroomUserRepository.existsByUserIdAndChatroomId(userId, chatroomId)) {
           throw new CustomException(ErrorCode.EXISTENT_CHATROOM_USER);
        }
        //교수는 타 채팅방에 입장 불가능
        if(professorRepository.existsById(userId))
        {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }
        User user= userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_USER));
        Chatroom chatroom=chatroomRepository.findById(chatroomId).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_CHATROOM));
        String nickName= chatroomEnterRequestDto.getNickName();
        ChatroomUser chatroomUser = new ChatroomUser(chatroom,user,nickName);
        chatroomUserRepository.save(chatroomUser);
    }
}
