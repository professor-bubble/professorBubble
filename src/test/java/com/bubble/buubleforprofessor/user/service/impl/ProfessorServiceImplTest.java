package com.bubble.buubleforprofessor.user.service.impl;

import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.entity.Professor;
import com.bubble.buubleforprofessor.user.entity.Role;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.repository.ProfessorRepository;
import com.bubble.buubleforprofessor.user.repository.RoleRepository;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    private UUID userId;
    private Professor professor;
    private User user;
    // 테스트용 역할 객체 (예시로 ROLE_USER 사용)
    private final Role professorRole = new Role("ROLE_USER");

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        University university = new University("Test University");

        user = User.builder()
                .id(userId)
                .name("John Doe")
                .phoneNumber("010-1111-1111")
                .email("johndoe@example.com")
                .university(university)
                .role(professorRole)
                .build();

        professor = Professor.builder()
                .user(user)
                .description("Test Professor")
                .department("Computer Science")
                .professorNum(12345)
                .isApproved(false)
                .build();
    }

    @DisplayName("교수 승인 성공 테스트")
    @Test
    void testSetApprovalStatus_Success() {
        // given
        when(professorRepository.findById(userId)).thenReturn(Optional.of(professor));

        // when
        professorService.setApprovalStatus(userId);

        // then
        assertTrue(professor.isApproved());
        verify(professorRepository, times(1)).save(professor);
    }

    @DisplayName("교수 ID가 존재하지 않는 경우 예외 발생 테스트")
    @Test
    void testSetApprovalStatus_ThrowsException_WhenProfessorNotFound() {
        // given
        when(professorRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> professorService.setApprovalStatus(userId));
        assertEquals(ErrorCode.NON_EXISTENT_USER, exception.getErrorCode());
    }

    @DisplayName("승인되지 않은 교수 페이지 반환 테스트")
    @Test
    void testGetApproveRequests_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Professor> professorList = List.of(professor);
        Page<Professor> professorPage = new PageImpl<>(professorList, pageable, professorList.size());
        when(professorRepository.findAllByIsApprovedFalse(pageable)).thenReturn(professorPage);

        // when
        Page<ApprovalRequestDto> result = professorService.getApproveRequests(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        ApprovalRequestDto dto = result.getContent().get(0);
        assertEquals("Test University", dto.getUniversityName());
        assertEquals("Computer Science", dto.getDepartment());
        assertEquals(12345, dto.getProfessorNum());
        assertEquals("John Doe", dto.getProfessorName());
        assertEquals("johndoe@example.com", dto.getEmail());
    }

    @DisplayName("승인 요청이 없는 경우 빈 페이지 반환 테스트")
    @Test
    void testGetApproveRequests_ReturnsEmptyPage_WhenNoPendingRequests() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Professor> emptyPage = Page.empty(pageable);
        when(professorRepository.findAllByIsApprovedFalse(pageable)).thenReturn(emptyPage);

        // when
        Page<ApprovalRequestDto> result = professorService.getApproveRequests(pageable);

        // then
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @DisplayName("교수승인 거절 성공")
    @Test
    void testDeleteApprovalById_Success() {
        // given
        when(professorRepository.existsById(userId)).thenReturn(true);

        // when
        professorService.deleteApprovalById(userId);

        // then
        verify(professorRepository, times(1)).deleteById(userId);
    }

    @DisplayName("교수승인 요청 거절 실패 - 교수데이터 존재하지않음")
    @Test
    void testDeleteApprovalById_ProfessorNotFound() {
        // given
        when(professorRepository.existsById(userId)).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> professorService.deleteApprovalById(userId));
        assertEquals(ErrorCode.NON_EXISTENT_USER, exception.getErrorCode());
    }

    @DisplayName("교수 삭제 성공")
    @Test
    void testDeleteById_Success() {
        // given
        when(professorRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(professorRole));

        // when
        professorService.deleteById(userId);

        // then
        verify(professorRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findByName("ROLE_USER");
    }

    @DisplayName("교수 ID가 존재하지 않으면 예외 발생")
    @Test
    void testDeleteById_ThrowsException_WhenProfessorNotFound() {
        // given
        when(professorRepository.existsById(userId)).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> professorService.deleteById(userId));
        assertEquals(ErrorCode.NON_EXISTENT_USER, exception.getErrorCode());
    }

    @DisplayName("ROLE_USER 역할이 존재하지 않으면 예외 발생")
    @Test
    void testDeleteById_ThrowsException_WhenRoleNotFound() {
        // given
        when(professorRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> professorService.deleteById(userId));
        assertEquals(ErrorCode.NON_EXISTENT_ROLE, exception.getErrorCode());
    }

    @DisplayName("교수 승인 요청 성공 - 교수 데이터 Create")
    @Test
    void testCreateProfessor_Success() {
        // 목 객체 생성
        Professor professor = mock(Professor.class);

        // 목 객체의 메서드 호출 시 동작 정의
        when(professorRepository.existsById(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        ApprovalRequestCreateDto approvalRequestCreateDto = new ApprovalRequestCreateDto();
        approvalRequestCreateDto.setProfessorNum(12345);
        approvalRequestCreateDto.setDescription("컴공교수");
        approvalRequestCreateDto.setDepartment("컴퓨터공학과");

        // 서비스 메서드 호출
        professorService.createProfessor(userId, approvalRequestCreateDto);

        // 메서드 호출 검증
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

}
