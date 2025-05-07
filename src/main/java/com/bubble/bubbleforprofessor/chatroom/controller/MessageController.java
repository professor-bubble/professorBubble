package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;

import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import com.bubble.buubleforprofessor.global.tracker.ChatSessionTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChatroomUserService chatRoomUserService;
    private final ChatSessionTracker tracker;
    //todo 카프카 이용해야함
    @MessageMapping("/message")
    public void sendMessage(@Payload MessageRequestDto message) {
        chatRoomUserService.exists(message.getUserId(),message.getChatRoomId());
        //todo 이거 제대로 작동안함.
        //나갔는데 연결이 안끊겨?
        boolean professorOnline = tracker.isProfessorOnline((long) message.getChatRoomId());
        messageService.sendAndHandleRead(message, professorOnline);
    }
}
