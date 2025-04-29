package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.bubbleforprofessor.university.entity.University;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import com.bubble.bubbleforprofessor.user.entity.Role;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatroomUserServiceImplTest {

    @Mock
    private ChatroomRepository chatroomRepository;

    @Mock
    private ChatroomUserRepository chatroomUserRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatroomUserServiceImpl chatroomUserService;

    @Mock
    private ProfessorRepository professorRepository;

    @BeforeEach
    void setUp(){

    }
    @DisplayName("채팅방 입장시 채팅방 생성")
    @Test
    void testCreateChatroomUser()
    {
        University university = University.builder()
                .universityName("조선대학교")
                .build();

        Role professorRole = new Role("PROFESSOR");
        Role userRole = new Role("USER");
        //교수
        User user =User.builder()
                .id(UUID.randomUUID())
                .loginId("abc")
                .password("abc")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastLoginAt(Timestamp.valueOf(LocalDateTime.now()))
                .name("창환")
                .phoneNumber("010-1111-1111")
                .email("example1@naver.com")
                .university(university)
                .role(professorRole).build();

        Professor professor= Professor.builder()
                .user(user)
                .description("전자공학부 교수입니다.")
                .professorNum(12345)
                .isApproved(true)
                .department("전자공학부")
                .build();
        User user1 =User.builder()
                .id(UUID.randomUUID())
                .loginId("abcd")
                .password("abcd")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastLoginAt(Timestamp.valueOf(LocalDateTime.now()))
                .name("홍길동")
                .phoneNumber("010-1112-1111")
                .email("example2@naver.com")
                .university(university)
                .role(userRole).build();

        UUID userId= user1.getId();
        Chatroom chatroom=new Chatroom(professor);
        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId,1)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(chatroomRepository.findById(1)).thenReturn(Optional.of(chatroom));


        chatroomUserService.createChatroomUser(userId,1,new ChatroomEnterRequestDto("nick"));

        // then
        verify(chatroomUserRepository).existsByUserIdAndChatroomId(userId, 1);
        verify(userRepository).findById(userId);
        verify(chatroomRepository).findById(1);

        // ChatroomUser 저장이 의도대로 이루어졌는지 검증
        ArgumentCaptor<ChatroomUser> captor = ArgumentCaptor.forClass(ChatroomUser.class);
        verify(chatroomUserRepository).save(captor.capture());
        ChatroomUser savedChatroomUser = captor.getValue();

        assertEquals(chatroom, savedChatroomUser.getChatroom());
        assertEquals(user1, savedChatroomUser.getUser());
        assertEquals("nick", savedChatroomUser.getNickName());    }

}