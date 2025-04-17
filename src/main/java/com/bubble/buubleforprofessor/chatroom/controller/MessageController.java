package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;

import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChatroomUserService chatRoomUserService;

    //todo 카프카 이용해야함
    @MessageMapping("/message")
    public void sendMessage(@Payload MessageRequestDto message) {
        chatRoomUserService.exists(message.getUserId(),message.getChatRoomId());

        message = messageService.filtering(message);
        MessageMongo message1 = messageService.save(message);

        MessageSimpleDto simpleDto = new MessageSimpleDto();
        simpleDto.setUserId(message.getUserId());
        simpleDto.setUserName(message.getUserName());
        simpleDto.setType(message1.getMessageType());
        simpleDto.setContent(message1.getContent());
        simpleDto.setCreateAt(message1.getSendTime());

        String userRole = message.getRole();
        messageService.send(message.getChatRoomId(),userRole,simpleDto);
    }
}
