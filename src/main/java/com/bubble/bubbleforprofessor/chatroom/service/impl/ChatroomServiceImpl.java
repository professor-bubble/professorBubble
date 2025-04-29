package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomDetailResponseDto;
import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.bubbleforprofessor.chatroom.service.ChatroomService;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.bubbleforprofessor.user.dto.UserSimpleResponseDto;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
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

    //채팅방 상세 조회 메세지, 유저들 등
    //todo 리팩토링 필요할 듯
    @Override
    @Transactional(readOnly = true)
    public ChatroomDetailResponseDto findByUserIdAndChatRoomId(UUID userId, int chatRoomId) {
        if(!chatroomUserRepository.existsByUserIdAndChatroomId(userId,chatRoomId))
        {
            throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
        }
        Chatroom chatroom= chatroomRepository.findById(chatRoomId).orElseThrow(()-> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM));
        Professor professor = chatroom.getProfessor();
        long userCount= chatroomUserRepository.countByChatroomId(chatRoomId);
        List<MessageDto> messages;
        //교수가 채팅방 들어간 것이라면 채팅방 내 모든 데이터
        if(professorRepository.existsById(userId))
        {
            messages =messageRepository.findByChatroomUser_Chatroom(chatroom).stream()
                    .map(message -> MessageDto.builder()
                            .messageId(message.getId())
                            .sendUser(UserSimpleResponseDto.builder()
                                    .userId(message.getChatroomUser().getUser().getId())
                                    .userName(message.getChatroomUser().getUser().getName()).build())
                            .sendTime(message.getSendTime())
                            .content(message.getContent()).build())
                    .toList();
        }
        //유저라면 유저와 교수의 데이터만
        else
        {
            messages =messageRepository.findByChatroomUser_ChatroomAndChatroomUser_User_IdIn(chatroom,List.of(userId,professor.getId())).stream()
                    .map(message -> MessageDto.builder()
                            .messageId(message.getId())
                            .sendUser(UserSimpleResponseDto.builder()
                                    .userId(message.getChatroomUser().getUser().getId())
                                    .userName(message.getChatroomUser().getUser().getName()).build())
                            .sendTime(message.getSendTime())
                            .content(message.getContent()).build())
                    .toList();
        }

        ChatroomDetailResponseDto chatroomResponseDto= ChatroomDetailResponseDto.builder()
                .chatroomId(chatRoomId)
                .professorDto(ProfessorResponseDto.builder()
                        .professorId(professor.getId())
                        .professorName(professor.getUser().getName())
                        .professorImageUrl(professor.getProfessorImage().getUrl()).build())
                .createdAt(LocalDateTime.now())
                .users(userCount)
                .messages(messages)
                .build();

        return chatroomResponseDto;
    }
    //채팅방 리스트 조회
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
