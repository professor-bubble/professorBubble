package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.chatroom.dto.ChatroomResponseDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageResponseDto;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomService;
import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.service.SkinService;
import com.bubble.buubleforprofessor.user.dto.ApprovalRequestCreateDto;
import com.bubble.buubleforprofessor.user.dto.ProfessorResponseDto;
import com.bubble.buubleforprofessor.user.dto.UserSimpleResponseDto;
import com.bubble.buubleforprofessor.user.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfessorService professorService;

    @InjectMocks
    private UserController userController;

    @MockitoBean
    private SkinService skinService;

    @MockitoBean
    private ChatroomService chatroomService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("교수 승인요청 성공")
    @WithMockUser(roles = "USER")
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
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"description\":\"Request to approve professor\", \"professorNum\":12345, \"department\":\"Computer Science\"}"))
                .andExpect(status().isCreated());

        // Verify that the service method was called once
        verify(professorService, times(1)).createProfessor(userId, approvalRequestCreateDto);
    }
    @DisplayName("교수 승인요청 유효성검사 실패")
    @WithMockUser(roles = "USER")
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
                        .with(csrf())
                        .content("{\"description\":\"Request to approve professor\", \"professorNum\":0, \"department\":\"A very long department name that exceeds the limit\"}"))
                .andExpect(status().isBadRequest());  // 400 Bad Request
    }
    @DisplayName("유저 아이디에 해당하는 스킨 가져오기 성공")
    @WithMockUser(roles = "USER")
    @Test
    void findSkinsByUserId_Success() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        // Given
        SkinResponseDto skin1 = SkinResponseDto.builder()
                .skinId(1)
                .skinName("Cool Skin")
                .price(100)
                .skinDescription("This is a cool skin")
                .skinUrl(List.of("https://example.com/skin1.png"))
                .build();

        Page<SkinResponseDto> mockPage = new PageImpl<>(List.of(skin1), PageRequest.of(0, 10), 1);

        when(skinService.getSkinsByUserId(userId, PageRequest.of(0, 10)))
                .thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/users/{userId}/skin",userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .param("pageNum", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].skinId").value(1))
                .andExpect(jsonPath("$.content[0].skinName").value("Cool Skin"))
                .andExpect(jsonPath("$.content[0].price").value(100))
                .andExpect(jsonPath("$.content[0].skinDescription").value("This is a cool skin"))
                .andExpect(jsonPath("$.content[0].skinUrl[0]").value("https://example.com/skin1.png"));
    }

    @DisplayName("유저아이디에 해당하는 스킨리스트(페이징) 조회 - 비어있을시 noContent")
    @WithMockUser(roles = "USER")
    @Test
    void findSkinsByUserId_NoContent() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        // Given
        Page<SkinResponseDto> emptyPage = Page.empty();
        when(skinService.getSkinsByUserId(userId, PageRequest.of(0, 10)))
                .thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/users/{userId}/skin",userId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .param("pageNum", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
    @DisplayName("채팅방 조회 성공")
    @WithMockUser(roles = "USER")
    @Test
    void testGetChatroom_Success() throws Exception {
        // 테스트에 사용할 UUID와 채팅방 ID를 생성합니다.
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        int chatroomId = 1;

        // 모의 데이터를 생성합니다.
        ProfessorResponseDto professorDto = ProfessorResponseDto.builder()
                .professorId(UUID.randomUUID())
                .professorName("교수 이름")
                .professorImageUrl("http://example.com/image.jpg")
                .build();

        UserSimpleResponseDto userDto = UserSimpleResponseDto.builder()
                .userId(UUID.randomUUID())
                .userName("사용자 이름")
                .build();

        MessageResponseDto messageDto = MessageResponseDto.builder()
                .messageId(1L)
                .sendUser(userDto)
                .sendTime(LocalDateTime.now())
                .content("메시지 내용")
                .build();

        ChatroomResponseDto chatroomResponse = ChatroomResponseDto.builder()
                .chatroomId(chatroomId)
                .professorDto(professorDto)
                .createdAt(LocalDateTime.now())
                .users(Collections.singletonList(userDto))
                .messages(Collections.singletonList(messageDto))
                .build();

        // chatroomService의 findByUserIdAndChatRoomId 메서드가 호출될 때, 모의 데이터를 반환하도록 설정합니다.
        when(chatroomService.findByUserIdAndChatRoomId(userId, chatroomId)).thenReturn(chatroomResponse);

        // GET 요청을 수행하고 응답을 검증합니다.
        mockMvc.perform(get("/api/users/{userId}/chatroom/{chatroomId}", userId, chatroomId)
                        .header("X-USER-ID", jwtUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatroomId").value(chatroomId))
                .andExpect(jsonPath("$.professorDto.professorId").value(professorDto.getProfessorId().toString()))
                .andExpect(jsonPath("$.professorDto.professorName").value(professorDto.getProfessorName()))
                .andExpect(jsonPath("$.professorDto.professorImageUrl").value(professorDto.getProfessorImageUrl()))
                .andExpect(jsonPath("$.users[0].userId").value(userDto.getUserId().toString()))
                .andExpect(jsonPath("$.users[0].userName").value(userDto.getUserName()))
                .andExpect(jsonPath("$.messages[0].messageId").value(messageDto.getMessageId()))
                .andExpect(jsonPath("$.messages[0].sendUser.userId").value(userDto.getUserId().toString()))
                .andExpect(jsonPath("$.messages[0].sendUser.userName").value(userDto.getUserName()))
                .andExpect(jsonPath("$.messages[0].content").value(messageDto.getContent()));

        // chatroomService의 findByUserIdAndChatRoomId 메서드가 정확히 한 번 호출되었는지 검증합니다.
        verify(chatroomService, times(1)).findByUserIdAndChatRoomId(userId, chatroomId);
    }


    @DisplayName("스킨 적용 여부 변경 성공")
    @WithMockUser(roles = "USER")
    @Test
    void testModifySkinStatus_Success() throws Exception {
        UUID jwtUserId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        int skinId = 1;

        // skinService.modifySkinStatus(userId, skinId)가 호출되어 아무 예외가 발생하지 않음을 가정
        doNothing().when(skinService).modifySkinStatus(userId, skinId);

        // PATCH 요청 수행 및 검증
        mockMvc.perform(patch("/api/users/{userId}/skin/{skinId}", userId, skinId)
                        .header("X-USER-ID", jwtUserId.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 서비스 메서드가 정확히 한 번 호출되었는지 검증
        verify(skinService, times(1)).modifySkinStatus(userId, skinId);
    }

}
