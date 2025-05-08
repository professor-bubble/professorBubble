package com.bubble.bubbleforprofessor.user.service.impl;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.university.entity.University;
import com.bubble.bubbleforprofessor.university.service.UniversityService;
import com.bubble.bubbleforprofessor.user.dto.JoinRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserRequestDto;
import com.bubble.bubbleforprofessor.user.dto.UserResponseDto;
import com.bubble.bubbleforprofessor.user.entity.Role;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.RoleRepository;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import com.bubble.bubbleforprofessor.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    /*
    given
    when
    then*/

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
//    @Mock
//    private UniversityService universityService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private University university;
    private Role role;
    private UUID userId;
    private User mockUser;
    private UserRequestDto mockUserRequestDto;

    @BeforeEach
    void setUp() {
        university = new University(0L, "testUniversity", false);
        role = new Role("testRole");

        userId = UUID.randomUUID();
        mockUser = User.builder()
                .id(userId)
                .loginId("testuser")
                .name("테스트유저")
                .email("test@bubble.com")
                .phoneNumber("01012341234")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastLoginAt(Timestamp.valueOf(LocalDateTime.now()))
                .university(university) // 또는 mock으로 넣어도 됨
                .role(role) // 마찬가지
                .build();

        mockUserRequestDto = new UserRequestDto(
                userId.toString(),
                "updateName",
                "updatePassword",
                "01012341111",
                "updateEmail@bubble.com"
        );
    }

    @DisplayName("유저 등록 성공")
    @Test
    void createUser() {
        // given
        when(userRepository.existsByLoginId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(any());

        JoinRequestDto joinRequestDto = new JoinRequestDto(
                "loginID",
                "password",
                "userName",
                "email",
                "phoneNumber",
                0,
                "role",
                null,
                0,
                null
        );

        // when
        userService.createUser(joinRequestDto);

        // then
        verify(userRepository).save(any(User.class));
    }

    @DisplayName("유저 가져오기 성공")
    @Test
    void getUser() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));

        // when
        UserResponseDto result = userService.getUser(userId.toString());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLoginId()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@bubble.com");
    }

    @DisplayName("유저 가져오기 실패 - 유저 없음")
    @Test
    void getUser_fail() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                userService.getUser(userId.toString()));

        // then
        assertEquals(customException.getErrorCode(), ErrorCode.NON_EXISTENT_USER);

    }

    @DisplayName("유저 수정 성공")
    @Test
    void updateUser() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));

        // when
        userService.updateUser(userId.toString(), mockUserRequestDto);

        // then
        assertThat("updateName").isEqualTo(mockUser.getName());
    }

    @DisplayName("유저 이름 불일치")
    @Test
    void updateUser_fail1() {
        // given
        String wrongId = UUID.randomUUID().toString();

        // when & then
        CustomException customException = assertThrows(CustomException.class, () ->
                userService.updateUser(wrongId, mockUserRequestDto));

        // then
        assertEquals(ErrorCode.INVALID_REQUEST, customException.getErrorCode());
    }

    @DisplayName("유저 이름 없음")
    @Test
    void updateUser_fail2() {
        // given

        // when
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        CustomException customException = assertThrows(CustomException.class, () ->
                userService.updateUser(userId.toString(), mockUserRequestDto));

        // then
        assertEquals(ErrorCode.NON_EXISTENT_USER, customException.getErrorCode());
    }
}