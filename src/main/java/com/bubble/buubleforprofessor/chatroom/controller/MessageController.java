package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatroomUserService chatRoomUserService;

    //todo 카프카 이용해서 데이터 저장하는 기능 추가할것.
    //todo 이미지도 되도록 해야함.
    @MessageMapping("/message")
    public void sendMessage(@Payload MessageRequestDto message) {
        chatRoomUserService.exists(message.getUserId(),message.getChatRoomId());

        //아래 주석의 과정을 카프카로 넘겨야함.
        Message message1= Message.builder()
                        .chatroomUser(chatRoomUserService.getUserByUserIdAndChatroomId(message.getUserId(), message.getChatRoomId()))
                                .sendTime(LocalDateTime.now())
                                        .content(message.getContent()).build();

        messageRepository.save(message1);
        //

        MessageSimpleDto simpleDto = new MessageSimpleDto();
        simpleDto.setUserName(message.getUserName());
        simpleDto.setContent(message.getContent());
        simpleDto.setCreateAt(message1.getSendTime());

        messagingTemplate.convertAndSend("/sub/chatroom/" + message.getChatRoomId(), simpleDto);
    }
}
