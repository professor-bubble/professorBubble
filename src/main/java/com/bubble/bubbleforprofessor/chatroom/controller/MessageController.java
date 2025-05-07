package com.bubble.bubbleforprofessor.chatroom.controller;

import com.bubble.bubbleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.bubbleforprofessor.chatroom.service.MessageService;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.bubbleforprofessor.global.tracker.ChatSessionTracker;
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

        boolean professorOnline = tracker.isProfessorOnline((long) message.getChatRoomId());
        messageService.sendAndHandleRead(message, professorOnline);
    }
}
