package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomServiceImplTest {

    @Mock
    private ChatroomRepository chatroomRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ChatroomServiceImpl chatroomService;

    private Professor professor;

    @BeforeEach
    void setUp() {
        // 테스트용 UUID 생성
        UUID userId = UUID.randomUUID();

        // User 객체 생성
        User user = User.builder()
                .id(userId)
                .loginId("professor123")
                .password("password")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .name("Professor Kim")
                .phoneNumber("010-1234-5678")
                .email("professor@example.com")
                .build();

        // Professor 객체 생성
        professor = Professor.builder()
                .user(user)
                .description("Computer Science Professor")
                .professorNum(12345)
                .isApproved(false)
                .department("Computer Science")
                .build();
    }

    @DisplayName("채팅방 생성 성공 테스트")
    @Test
    void testCreateChatroom_Success() {
        // given
        when(chatroomRepository.existsChatroomByProfessor(professor)).thenReturn(false);

        // when
        chatroomService.createChatroom(professor);

        // then
        verify(chatroomRepository, times(1)).save(any(Chatroom.class));
    }

    @DisplayName("이미 존재하는 채팅방 예외 테스트")
    @Test
    void testCreateChatroom_ThrowsExceptionIfChatroomExists() {
        // given
        when(chatroomRepository.existsChatroomByProfessor(professor)).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> chatroomService.createChatroom(professor));
        assertEquals(ErrorCode.EXISTENT_CHATROOM, exception.getErrorCode());

        verify(chatroomRepository, never()).save(any(Chatroom.class));
    }
}
