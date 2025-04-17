package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageResponseDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageMongoRepository;
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
import java.util.Map;
import java.util.Set;
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
    private final MessageMongoRepository messageMongoRepository;

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

    @Override
    @Transactional(readOnly = true)
    public ChatroomResponseDto findByUserIdAndChatRoomId(UUID userId, int chatroomId) {
        if(!chatroomUserRepository.existsByUserIdAndChatroomId(userId,chatroomId))
        {
            throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
        }
        Chatroom chatroom= chatroomRepository.findById(chatroomId).orElseThrow(()-> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM));
        Professor professor = chatroom.getProfessor();
        //챗룸 유저와 유저 관계 entityGraph로 N+1 해결
        //채팅방에 참여하는 유저 모두 반환
        List<UserSimpleResponseDto> users= chatroomUserRepository.findByChatroomId(chatroomId).stream()
                .map(chatroomUser -> UserSimpleResponseDto.builder()
                        .userId(chatroomUser.getUser().getId())
                        .userName(chatroomUser.getNickName()).build())
                .toList();
        //채팅방 내 메세지 모두 조회. N+1 문제를 해결하기위해 Set과 Map 이용
        //todo 교수 , 유저 나눠서 유저는 자신과 교수꺼. 교수는 모두의 데이터.
        //챗룸 레파지토리에서 userId, chatroomId 통해서 존재하면 주인이니까 교수인거 인증.
        //존재하지 않으면 주인 아니니까 유저로 취급하기.
        List<MessageMongo> messageMongoList = messageMongoRepository.findMessagesByChatroomId(chatroomId);
        Set<UUID> userIdSet = messageMongoList.stream().map(MessageMongo::getUserId)
                .collect(Collectors.toSet());
        List<ChatroomUser> chatroomUserList= chatroomUserRepository.findByUserIdInAndChatroomId(userIdSet,chatroomId);

        Map<UUID, String> userNicknameMap = chatroomUserList.stream()
                .collect(Collectors.toMap(cu -> cu.getUser().getId(), ChatroomUser::getNickName));

        List<MessageResponseDto> messages=messageMongoList.stream().map(
                message -> {
                    String nickName= userNicknameMap.getOrDefault(message.getUserId(), "Unknown User");
                    return MessageResponseDto.builder()
                            .sendUser(UserSimpleResponseDto.builder()
                                    .userId(message.getUserId())
                                    .userName(nickName)
                                    .build())
                            .sendTime(message.getSendTime())
                            .content(message.getContent())
                            .build();
                }
        ).toList();

        ChatroomResponseDto chatroomResponseDto= ChatroomResponseDto.builder()
                .chatroomId(chatroomId)
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
