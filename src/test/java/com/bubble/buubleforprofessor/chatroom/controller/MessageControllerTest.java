package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    @MockitoBean
    private SimpMessageSendingOperations messagingTemplate;

    @MockitoBean
    private MessageRepository messageRepository;

    @MockitoBean
    private ChatroomUserService chatRoomUserService;

    private MessageRequestDto request;
    private User dummyUser;
    private Chatroom dummyChatroom;
    private ChatroomUser dummyChatroomUser;

    @BeforeEach
    public void setUp() {
        // MessageRequestDto 생성 및 값 설정
        request = new MessageRequestDto();
        request.setChatRoomId(1);
        request.setUserId(UUID.fromString("61000000-0000-0000-0000-000000000000"));
        request.setUserName("chang hwan");
        request.setContent("ㅎㅇㅇ");

        // 더미 User 생성 (필수 필드만 최소한으로 설정)
        dummyUser = User.builder()
                .id(UUID.fromString("61000000-0000-0000-0000-000000000000"))
                .loginId("dummyLogin")
                .password("dummyPass")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .name("chang hwan")
                .phoneNumber("010-0000-0000")
                .email("dummy@example.com")
                .build();

        // 더미 Chatroom 생성 - Professor는 필요하지 않으므로 null 전달 (실제 로직에 따라 수정)
        dummyChatroom = new Chatroom(null);

        // 더미 ChatroomUser 생성
        dummyChatroomUser = new ChatroomUser(dummyChatroom, dummyUser);
    }

    @Test
    public void testSendMessage() {
        // chatRoomUserService가 더미 ChatroomUser를 반환하도록 설정
        when(chatRoomUserService.getUserByUserIdAndChatroomId(any(UUID.class), anyInt()))
                .thenReturn(dummyChatroomUser);

        // MessageRepository.save()가 저장된 Message 객체를 반환하도록 설정
        LocalDateTime now = LocalDateTime.now();
        Message savedMessage = Message.builder()
                .chatroomUser(dummyChatroomUser)
                .sendTime(now)
                .content(request.getContent())
                .build();
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        // sendMessage() 호출
        messageController.sendMessage(request);

        // messagingTemplate.convertAndSend()가 올바른 경로와 MessageSimpleDto 객체로 호출되었는지 캡처
        ArgumentCaptor<MessageSimpleDto> captor = ArgumentCaptor.forClass(MessageSimpleDto.class);
        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/sub/chatroom/" + request.getChatRoomId()), captor.capture());

        MessageSimpleDto capturedDto = captor.getValue();
        assertEquals("chang hwan", capturedDto.getUserName(), "User name should match");
        assertEquals("ㅎㅇㅇ", capturedDto.getContent(), "Content should match");
        assertNotNull(capturedDto.getCreateAt(), "createAt should not be null");
    }
}
