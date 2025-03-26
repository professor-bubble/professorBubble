package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomRepository;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.entity.ProfessorImage;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
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
        chatroomUser =new ChatroomUser(chatroom,user);
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

    @DisplayName("채팅방 사용자 존재 시 채팅방 정보 조회 성공 테스트")
    @Test
    void testFindByUserIdAndChatRoomId_Success() {
        // given
        when(chatroomUserRepository.existsByUserIdAndChatroomId(userId, chatRoomId)).thenReturn(true);
        when(chatroomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatroom));
        when(chatroomUserRepository.findByChatroomId(chatRoomId)).thenReturn(Collections.emptyList());
        when(messageRepository.findByChatroomUser_Chatroom(chatroom)).thenReturn(Collections.emptyList());

        // when
        ChatroomResponseDto response = chatroomService.findByUserIdAndChatRoomId(userId, chatRoomId);

        // then
        assertNotNull(response);
        assertEquals(chatRoomId, response.getChatroomId());
        verify(chatroomUserRepository, times(1)).existsByUserIdAndChatroomId(userId, chatRoomId);
        verify(chatroomRepository, times(1)).findById(chatRoomId);
        verify(chatroomUserRepository, times(1)).findByChatroomId(chatRoomId);
        verify(messageRepository, times(1)).findByChatroomUser_Chatroom(chatroom);
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
        verify(chatroomUserRepository, never()).findByChatroomId(anyInt());
        verify(messageRepository, never()).findByChatroomUser_Chatroom(any(Chatroom.class));
    }
}
