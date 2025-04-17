package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;

import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import com.bubble.buubleforprofessor.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private ChatroomUserService chatRoomUserService;


    private MessageRequestDto request;
    private User dummyUser;
    private Chatroom dummyChatroom;
    private ChatroomUser dummyChatroomUser;



    @BeforeEach
    public void setUp() {
        request = new MessageRequestDto();
        request.setChatRoomId(1);
        request.setUserId(UUID.fromString("61000000-0000-0000-0000-000000000000"));
        request.setUserName("chang hwan");
        request.setContent("ㅎㅇㅇ");
        request.setRole("PROFESSOR");
    }

    @Test
    public void testSendMessage() {
        // Mocking the filtering method
        when(messageService.filtering(any(MessageRequestDto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mocking the save method
        MessageMongo savedMessage = MessageMongo.builder()
                .userId(request.getUserId())
                .chatroomId(request.getChatRoomId())
                .sendTime(LocalDateTime.now())
                .content(request.getContent())
                .messageType(Message.MessageType.valueOf("TEXT"))
                .build();
        when(messageService.save(any(MessageRequestDto.class))).thenReturn(savedMessage);

        // Invoke the method under test
        messageController.sendMessage(request);

        // Verify that the dependencies were called as expected
        verify(chatRoomUserService).exists(request.getUserId(), request.getChatRoomId());
        verify(messageService).filtering(request);
        verify(messageService).save(request);
        verify(messageService).send(eq(request.getChatRoomId()), eq(request.getRole()), any(MessageSimpleDto.class));
    }
}
