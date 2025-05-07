package com.bubble.bubbleforprofessor.chatroom.controller;

import com.bubble.bubbleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.bubbleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;

import com.bubble.bubbleforprofessor.chatroom.entity.Message;
import com.bubble.bubbleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.bubbleforprofessor.chatroom.service.MessageService;
import com.bubble.bubbleforprofessor.global.tracker.ChatSessionTracker;
import com.bubble.bubbleforprofessor.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @MockitoBean
    private ChatSessionTracker chatSessionTracker;

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
    }


    @Test
    public void testSendMessage() {
        // given
        when(chatSessionTracker.isProfessorOnline(1L)).thenReturn(false);

        // when
        messageController.sendMessage(request);

        // then
        verify(chatRoomUserService).exists(request.getUserId(), request.getChatRoomId());
        verify(messageService).sendAndHandleRead(request, false);
    }
}
