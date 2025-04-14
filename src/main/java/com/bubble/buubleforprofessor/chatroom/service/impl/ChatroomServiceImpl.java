package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomDetailResponseDto;
import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    public Chatroom createChatroom(Professor professor) {
        if(chatroomRepository.existsChatroomByProfessor(professor))
        {
            throw new CustomException(ErrorCode.EXISTENT_CHATROOM);
        }
        Chatroom chatroom = new Chatroom(professor);
        return chatroomRepository.save(chatroom);
    }

    //todo queryDSL 고려해볼 것 N+1문제 필히 해결해야함.
    @Override
    @Transactional(readOnly = true)
    public ChatroomDetailResponseDto findByUserIdAndChatRoomId(UUID userId, int chatRoomId) {
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

        ChatroomDetailResponseDto chatroomResponseDto= ChatroomDetailResponseDto.builder()
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

    @Override
    public List<ChatroomResponseDto> findAllChatroomByUserId(UUID userID) {
        List<ChatroomUser> chatroomList = chatroomUserRepository.findAllByUserId(userID);
        List<ChatroomResponseDto> chatroomResponseDtoList = chatroomList.stream()
                .map(chatroomUser ->
                    ChatroomResponseDto.builder()
                            .chatroomId(chatroomUser.getChatroom().getId())
                            .createTime(chatroomUser.getChatroom().getCreatedAt())
                            .lastSeenAt(chatroomUser.getChatroom().getLastSeenAt())
                            .professor(ProfessorResponseDto.builder()
                                    .professorId(userID)
                                    .professorName(chatroomUser.getChatroom().getProfessor().getUser().getName())
                                    .professorImageUrl(chatroomUser.getChatroom().getProfessor().getProfessorImage().getUrl())
                                    .build())
                            .build()
                ).toList();
        return chatroomResponseDtoList;
    }

}
