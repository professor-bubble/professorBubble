package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import com.bubble.buubleforprofessor.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubAdminController.class)
class SubAdminControllerTest {

    @MockitoBean
    private ProfessorService professorService;

    @MockitoBean
    private UserService userService;

    @InjectMocks
    private SubAdminController subAdminController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    // 테스트: approve-requests 엔드포인트 (유효한 X-USER-ID와 페이징 파라미터 사용)
    @DisplayName("교수 승인요청리스트 조회 성공")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testApproveRequests_Success() throws Exception {
        // Given: 관리자의 아이디와 DTO 생성
        UUID userId = UUID.randomUUID();
        UUID dtoUserId = UUID.randomUUID(); // DTO에 넣을 사용자 ID
        List<ApprovalRequestDto> approvalRequestDtos = Arrays.asList(
                ApprovalRequestDto.builder()
                        .userId(dtoUserId)
                        .universityName("University A")
                        .department("Computer Science")
                        .professorNum(1)
                        .professorName("Prof. A")
                        .phoneNumber("010-1234-5678")
                        .email("profA@university.com")
                        .build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<ApprovalRequestDto> page = new PageImpl<>(approvalRequestDtos, pageable, approvalRequestDtos.size());
        when(professorService.getApproveRequests(any(Pageable.class))).thenReturn(page);

        // Then: 반환된 JSON 객체 내 "content" 배열의 각 필드 값이 기대한 값과 일치하는지 비교
        mockMvc.perform(get("/api/sub-admin/approve-requests")
                        .header("X-USER-ID", userId.toString())
                        .param("pageNum", "0")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].userId").value(dtoUserId.toString()))
                .andExpect(jsonPath("$.content[0].universityName").value("University A"))
                .andExpect(jsonPath("$.content[0].department").value("Computer Science"))
                .andExpect(jsonPath("$.content[0].professorNum").value(1))
                .andExpect(jsonPath("$.content[0].professorName").value("Prof. A"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.content[0].email").value("profA@university.com"));
    }

    // 테스트: approve-requests 엔드포인트 (빈 페이지 반환)
    @DisplayName("교수 승인요청 빈페이지 반환")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testApproveRequests_NoContent() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ApprovalRequestDto> emptyPage = Page.empty(pageable);
        when(professorService.getApproveRequests(any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/sub-admin/approve-requests")
                        .header("X-USER-ID", userId.toString())
                        .param("pageNum", "0")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // 테스트: approveRequest 엔드포인트 (유효한 X-USER-ID 및 userId)
    @DisplayName("교수 승인 성공")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testApproveRequest_Success() throws Exception {
        // Given
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // When
        mockMvc.perform(patch("/api/sub-admin/{userId}/approve", userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Then
        verify(professorService, times(1)).setApprovalStatus(userId);
    }

    // 테스트: approveRequest 엔드포인트 (유효하지 않은 userId)
    @DisplayName("교수 승인 실패 - 유효하지 않은 userId에 대한 요청")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testApproveRequest_InvalidUserId() throws Exception {
        // Given
        UUID jwtUserId = UUID.randomUUID();
        UUID invalidUserId = null;

        // When
        mockMvc.perform(patch("/api/sub-admin/{userId}/approve", invalidUserId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // 테스트: approveRequest 엔드포인트 (유효하지 않은 X-USER-ID)
    @DisplayName("교수 승인 실패 - 잘못된 X-USER-ID")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testApproveRequest_InvalidXUserId() throws Exception {
        // Given
        UUID invalidUserId = UUID.randomUUID();

        // When
        mockMvc.perform(patch("/api/sub-admin/{userId}/approve", invalidUserId)
                        .header("X-USER-ID", "invalid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // 교수 승인 삭제 요청 성공
    @DisplayName("교수 승인 삭제 성공")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testDeleteApproveRequest_Success() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/sub-admin/{userId}/approve", userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(professorService, times(1)).deleteApprovalById(userId);
    }

    // 교수 삭제 요청 성공
    @DisplayName("교수 삭제 성공")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testDeleteProfessor_Success() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/sub-admin/{userId}/professor", userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf())) // CSRF 토큰을 포함
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(professorService, times(1)).deleteById(userId);
    }

    // 잘못된 교수 삭제 요청 (잘못된 userId)
    @DisplayName("교수 삭제 실패 - 잘못된 userId")
    @WithMockUser(username = "testUser", roles = {"SUB_ADMIN"})
    @Test
    void testDeleteProfessor_InvalidUserId() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID invalidUserId = null;

        mockMvc.perform(delete("/api/sub-admin/{userId}/professor", invalidUserId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
