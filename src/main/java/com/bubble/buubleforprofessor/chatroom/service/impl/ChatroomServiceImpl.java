package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ProfessorRepository professorRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public void createChatroom(Professor professor) {
        if(chatroomRepository.existsChatroomByProfessor(professor))
        {
            throw new CustomException(ErrorCode.EXISTENT_CHATROOM);
        }
        Chatroom chatroom = new Chatroom(professor);
        chatroomRepository.save(chatroom);
    }

    //todo queryDSL 고려해볼 것 N+1문제 필히 해결해야함.
    @Override
    @Transactional(readOnly = true)
    public ChatroomResponseDto findByUserIdAndChatRoomId(UUID userId, int chatRoomId) {
        if(!chatroomUserRepository.existsByUserIdAndChatroomId(userId,chatRoomId))
        {
            throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
        }
        Chatroom chatroom= chatroomRepository.findById(chatRoomId).orElseThrow(()-> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM));
        Professor professor = chatroom.getProfessor();
        List<UserSimpleResponseDto> users= chatroomUserRepository.findByChatroomId(chatRoomId).stream()
                .map(chatroomUser -> UserSimpleResponseDto.builder()
                        .userId(chatroomUser.getUser().getId())
                        .userName(chatroomUser.getUser().getName()).build())
                .toList();
        List<MessageDto> messages =messageRepository.findByChatroomUser_Chatroom(chatroom).stream()
                .map(message -> MessageDto.builder()
                        .messageId(message.getId())
                        .sendUser(UserSimpleResponseDto.builder()
                                .userId(message.getChatroomUser().getUser().getId())
                                .userName(message.getChatroomUser().getUser().getName()).build())
                        .sendTime(message.getSendTime())
                        .content(message.getContent()).build())
                .toList();

        ChatroomResponseDto chatroomResponseDto= ChatroomResponseDto.builder()
                .chatroomId(chatRoomId)
                .professorDto(ProfessorResponseDto.builder()
                        .professorId(professor.getId())
                        .professorName(professor.getUser().getName())
                        .professorImageUrl(professor.getProfessorImage().getUrl()).build())
                .createdAt(LocalDateTime.now())
                .users(users)
                .messages(messages)
                .build();

        return chatroomResponseDto;
    }

}
