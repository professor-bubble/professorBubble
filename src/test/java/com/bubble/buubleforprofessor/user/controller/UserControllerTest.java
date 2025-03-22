package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DisplayName("교수 승인요청 성공")
    @Test
    void testApproveRequest() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // ApprovalRequestCreateDto 객체 생성
        ApprovalRequestCreateDto approvalRequestCreateDto = new ApprovalRequestCreateDto();
        approvalRequestCreateDto.setDescription("Request to approve professor");
        approvalRequestCreateDto.setProfessorNum(12345);
        approvalRequestCreateDto.setDepartment("Computer Science");

        // Given
        doNothing().when(professorService).createProfessor(userId, approvalRequestCreateDto);

        // When & Then
        mockMvc.perform(post("/api/users/{userId}/approve-request", userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .contentType("application/json")
                        .content("{\"description\":\"Request to approve professor\", \"professorNum\":12345, \"department\":\"Computer Science\"}"))
                .andExpect(status().isOk());

        // Verify that the service method was called once
        verify(professorService, times(1)).createProfessor(userId, approvalRequestCreateDto);
    }
    @DisplayName("교수 승인요청 유효성검사 실패")
    @Test
    void testApproveRequest_ValidationFails() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // 잘못된 ApprovalRequestCreateDto (professorNum이 없거나 department가 20자를 초과)
        ApprovalRequestCreateDto invalidDto = new ApprovalRequestCreateDto();
        invalidDto.setDescription("Request to approve professor");
        invalidDto.setProfessorNum(0);  // Invalid number
        invalidDto.setDepartment("A very long department name that exceeds the limit");

        // When & Then
        mockMvc.perform(post("/api/users/{userId}/approve-request", userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .contentType("application/json")
                        .content("{\"description\":\"Request to approve professor\", \"professorNum\":0, \"department\":\"A very long department name that exceeds the limit\"}"))
                .andExpect(status().isBadRequest());  // 400 Bad Request
    }
}
