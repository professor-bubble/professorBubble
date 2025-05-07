package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.bubbleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    public void exists(UUID userId, int RoomId) {
        if(!chatroomUserRepository.existsByUserIdAndChatroomId(userId, RoomId)) {
            throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
        }
    }
    //todo 이를 이용한 캐싱은 변경감지가 불가능하다. 그래서 CacheEvict과 같은 방법으로 객체가 변경될 시 캐시를 삭제해 주어야함.
    @Cacheable(value = "chatroom_users", key = "#userId + '_' + #chatroomId")
    @Override
    public ChatroomUser getUserByUserIdAndChatroomId(UUID userId, int chatroomId) {
        System.out.println("DB에서 사용자 정보를 조회합니다. userId="+userId+" chatroomId="+ chatroomId);
        ChatroomUser chatroomUser=chatroomUserRepository.findByUserIdAndChatroomId(userId,chatroomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER));
        return chatroomUser;
    }

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
