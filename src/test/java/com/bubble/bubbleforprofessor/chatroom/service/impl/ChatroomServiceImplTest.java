package com.bubble.bubbleforprofessor.chatroom.service.impl;

import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomDetailResponseDto;
import com.bubble.bubbleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.entity.Message;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.bubbleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import com.bubble.bubbleforprofessor.user.entity.ProfessorImage;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.ProfessorRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatroomUserRepository chatroomUserRepository;

    @Mock
    private MessageRepository messageRepository;
    private UUID userId;
    private int chatRoomId;
    private User user;

    private Professor professor;
    private Chatroom chatroom;
    private ChatroomUser chatroomUser;
    private ProfessorImage professorImage;
    private Message message;
    @BeforeEach
    void setUp() {
        // 테스트용 UUID 생성
        UUID userId = UUID.randomUUID();

        // User 객체 생성
        user = User.builder()
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
                .id(userId)
                .user(user)
                .description("Computer Science Professor")
                .professorNum(12345)
                .isApproved(false)
                .department("Computer Science")
                .build();

        chatroom = new Chatroom(professor);

        professorImage = ProfessorImage.builder()
                .url("abc.png")
                .professor(professor).build();
        professor.modifyProfessorImage(professorImage);
        chatroomUser =new ChatroomUser(chatroom,user,"코딩 공부 중");
        message = Message.builder()
                        .chatroomUser(chatroomUser)
                                .sendTime(LocalDateTime.now())
                                        .content("Test Message").build();
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

    @DisplayName("채팅방 사용자 존재하지 않을 시 예외 발생 테스트")
    @Test
    void testFindByUserIdAndChatRoomId_UserNotInChatroom() {
        // given
        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId, chatRoomId)).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> chatroomService.findByUserIdAndChatRoomId(userId, chatRoomId));
        assertEquals(ErrorCode.NON_EXISTENT_CHATROOM_USER, exception.getErrorCode());

        verify(chatroomUserRepository, times(1)).existsByUserIdAndChatroomId(userId, chatRoomId);
        verify(chatroomRepository, never()).findById(anyInt());
        verify(messageRepository, never()).findByChatroomUser_Chatroom(any(Chatroom.class));
    }

    @Test
    @DisplayName("교수인 경우 채팅방 상세 조회 성공")
    void testFindByUserIdAndChatRoomId_AsProfessor() {
        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId, chatRoomId)).thenReturn(true);
        when(chatroomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatroom));
        when(chatroomUserRepository.countByChatroomId(chatRoomId)).thenReturn(1L);
        when(professorRepository.existsById(userId)).thenReturn(true);
        when(messageRepository.findByChatroomUser_Chatroom(chatroom)).thenReturn(List.of(message));

        ChatroomDetailResponseDto result = chatroomService.findByUserIdAndChatRoomId(userId, chatRoomId);

        assertNotNull(result);
        assertEquals(chatRoomId, result.getChatroomId());
        assertEquals("Professor Kim", result.getProfessorDto().getProfessorName());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    @DisplayName("교수가 아닌 경우 메시지 제한 조회 성공")
    void testFindByUserIdAndChatRoomId_AsStudent() {
        UUID studentId = UUID.randomUUID();

        when(chatroomUserRepository.existsByUserIdAndChatroomId(studentId, chatRoomId)).thenReturn(true);
        when(chatroomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatroom));
        when(chatroomUserRepository.countByChatroomId(chatRoomId)).thenReturn(1L);
        when(messageRepository.findByChatroomUser_ChatroomAndChatroomUser_User_IdIn(eq(chatroom), anyList()))
                .thenReturn(List.of(message));

        ChatroomDetailResponseDto result = chatroomService.findByUserIdAndChatRoomId(studentId, chatRoomId);

        assertNotNull(result);
        assertEquals(1, result.getMessages().size());
    }

    @Test
    @DisplayName("채팅방 리스트 조회 테스트")
    void testFindAllChatroomByUserId() {
        List<ChatroomUser> chatroomUsers = List.of(chatroomUser);

        when(chatroomUserRepository.findAllByUserId(userId)).thenReturn(chatroomUsers);

        // when
        List<ChatroomResponseDto> response = chatroomService.findAllChatroomByUserId(userId);

        // then
        assertNotNull(response);
        assertEquals(1, response.size());
        ChatroomResponseDto dto = response.get(0);
        assertEquals(chatroom.getId(), dto.getChatroomId());
        assertEquals(chatroom.getCreatedAt(), dto.getCreateTime());
        assertEquals(chatroom.getLastSeenAt(), dto.getLastSeenAt());
        assertEquals(professor.getUser().getName(), dto.getProfessor().getProfessorName());
        assertEquals(professorImage.getUrl(), dto.getProfessor().getProfessorImageUrl());

        verify(chatroomUserRepository, times(1)).findAllByUserId(userId);
    }


}
