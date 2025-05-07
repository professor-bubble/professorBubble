package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
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
}
