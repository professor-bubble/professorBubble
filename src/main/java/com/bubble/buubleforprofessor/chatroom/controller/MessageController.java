package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;

import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageService messageService;
    private final ChatroomUserService chatRoomUserService;

    //todo 카프카 이용해야함
    @MessageMapping("/message")
    public void sendMessage(@Payload MessageRequestDto message) {
        chatRoomUserService.exists(message.getUserId(),message.getChatRoomId());

        MessageMongo message1 = messageService.save(message);


        MessageSimpleDto simpleDto = new MessageSimpleDto();
        simpleDto.setUserName(message.getUserName());
        simpleDto.setType(message1.getMessageType());
        simpleDto.setContent(message1.getContent());
        simpleDto.setCreateAt(message1.getSendTime());
        //유저는 채팅방 구독 , 자기자신 구독.
        //교수는 자기자신 구독
        if(message.getRole().equals("PROFESSOR"))
        {
            //교수가 보내는곳. 채팅방과(유저 전체) 교수(자신)
            messagingTemplate.convertAndSend("/sub/chatroom/" + message.getChatRoomId(), simpleDto);
            messagingTemplate.convertAndSend("/sub/chatroom/" + message.getChatRoomId()+"/professor", simpleDto);
        }
        else
        {
            //유저가 보내는곳.교수 유저(자신)
            messagingTemplate.convertAndSend("/sub/chatroom/" + message.getChatRoomId()+"/professor", simpleDto);
            messagingTemplate.convertAndSend("/sub/user/" + message.getUserId(),simpleDto);
        }
    }
}
