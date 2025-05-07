package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomEnterRequestDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.university.entity.University;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import com.bubble.bubbleforprofessor.user.entity.Role;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.service.impl.ChatroomUserServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatroomUserServiceImplTest {

    @Mock
    private ChatroomUserRepository chatroomUserRepository;

    @InjectMocks
    private ChatroomUserServiceImpl chatroomUserService;

    @DisplayName("유저가 존재하는 채팅방일 시 에러를 내지않음.")
    @Test
    public void testExists_UserExists_NoException() {
        // Given
        UUID userId = UUID.fromString("61000000-0000-0000-0000-000000000000");
        int roomId = 1;

        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId, roomId)).thenReturn(true);

        // When & Then: 예외 없이 호출되어야 함
        assertDoesNotThrow(() -> chatroomUserService.exists(userId, roomId));
        verify(chatroomUserRepository, times(1)).existsByUserIdAndChatroomId(userId, roomId);
    }
    @DisplayName("유저가 존재하는 채팅방이 아닐 시 에러를 던짐")
    @Test
    public void testExists_UserNotExists_ThrowsException() {
        // Given
        UUID userId = UUID.fromString("61000000-0000-0000-0000-000000000000");
        int roomId = 1;

        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId, roomId)).thenReturn(false);

        // When & Then: CustomException 발생 확인
        CustomException ex = assertThrows(CustomException.class, () -> chatroomUserService.exists(userId, roomId));
        assertEquals(ErrorCode.NON_EXISTENT_CHATROOM_USER, ex.getErrorCode());
        verify(chatroomUserRepository, times(1)).existsByUserIdAndChatroomId(userId, roomId);
    }
    @DisplayName("유저가 속한 채팅방일 시 ChatroomUser 객체 가져오기 성공")
    @Test
    public void testGetUserByUserIdAndChatroomId_UserExists_ReturnsChatroomUser() {
        // Given
        UUID userId = UUID.fromString("61000000-0000-0000-0000-000000000000");
        int chatroomId = 1;

        // 더미 ChatroomUser 객체를 목으로 생성 (실제 엔티티 대신 사용)
        ChatroomUser dummyChatroomUser = mock(ChatroomUser.class);
        when(chatroomUserRepository.findByUserIdAndChatroomId(userId, chatroomId))
                .thenReturn(Optional.of(dummyChatroomUser));

        // When
        ChatroomUser result = chatroomUserService.getUserByUserIdAndChatroomId(userId, chatroomId);

        // Then
        assertNotNull(result);
        assertEquals(dummyChatroomUser, result);
        verify(chatroomUserRepository, times(1)).findByUserIdAndChatroomId(userId, chatroomId);
    }
    @DisplayName("유저가 속한 채팅방이 아닐 시 에러")
    @Test
    public void testGetUserByUserIdAndChatroomId_UserNotExists_ThrowsException() {
        // Given
        UUID userId = UUID.fromString("61000000-0000-0000-0000-000000000000");
        int chatroomId = 1;

        when(chatroomUserRepository.findByUserIdAndChatroomId(userId, chatroomId))
                .thenReturn(Optional.empty());

        // When & Then: CustomException 발생 확인
        CustomException ex = assertThrows(CustomException.class,
                () -> chatroomUserService.getUserByUserIdAndChatroomId(userId, chatroomId));
        assertEquals(ErrorCode.NON_EXISTENT_CHATROOM_USER, ex.getErrorCode());
        verify(chatroomUserRepository, times(1)).findByUserIdAndChatroomId(userId, chatroomId);
    }
}
