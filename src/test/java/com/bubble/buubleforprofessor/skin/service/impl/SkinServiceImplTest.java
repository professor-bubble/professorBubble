package com.bubble.buubleforprofessor.skin.service.impl;

import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.entity.Category;
import com.bubble.buubleforprofessor.skin.entity.Skin;
import com.bubble.buubleforprofessor.skin.entity.SkinImage;
import com.bubble.buubleforprofessor.skin.entity.UserSkin;
import com.bubble.buubleforprofessor.skin.repository.SkinRepository;
import com.bubble.buubleforprofessor.skin.repository.UserSkinRepository;
import com.bubble.buubleforprofessor.university.entity.University;
import com.bubble.buubleforprofessor.user.entity.Role;
import com.bubble.buubleforprofessor.user.entity.User;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkinServiceImplTest {

    @Mock
    private UserSkinRepository userSkinRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SkinServiceImpl skinService;

    private Skin skin;
    private SkinImage skinImage1;
    private SkinImage skinImage2;
    private User user;
    private Role role = new Role("ROLE_USER");
    private University university = new University("조선대학교");
    private Category category = new Category("글꼴");
    private UUID userId;
    private UserSkin userSkin;// UserSkin mock object
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .phoneNumber("010-1111-1111")
                .email("johndoe@example.com")
                .university(university)
                .role(role)
                .build();
        userId= user.getId();

        skin=Skin.builder()
                .price(5000)
                .category(category)
                .isDelete(false)
                .description("바탕글")
                .name("바탕글")
                .build();
        skinImage1=new SkinImage(skin,"a.jpg");
        skinImage2=new SkinImage(skin,"b.jpg");

        List<SkinImage> images=Arrays.asList(skinImage1,skinImage2);
        skin.modifySkinImages(images);

        userSkin= new UserSkin(skin,user,false);
    }

    @DisplayName("유저아이디에 해당하는 스킨 가져오기 성공")
    @Test
    void getSkinsByUserId_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userSkinRepository.getSkinsByUserId(userId, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(userSkin)));  // 이 부분 추가

        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<SkinResponseDto> result = skinService.getSkinsByUserId(userId, pageRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());  // 이제 비어 있지 않도록 해야 함
        assertEquals(1, result.getTotalElements());

        // Check if SkinResponseDto was correctly mapped
        SkinResponseDto skinResponseDto = result.getContent().get(0);
        assertEquals(skin.getName(), skinResponseDto.getSkinName());
        assertEquals(skin.getPrice(), skinResponseDto.getPrice());
        assertEquals(skin.getDescription(), skinResponseDto.getSkinDescription());
        assertEquals(2, skinResponseDto.getSkinUrl().size());  // Check if both images are included
    }

    @DisplayName("유저아이디에 해당하는 스킨 가져오기 실패 - 유저가 존재하지않음")
    @Test
    void getSkinsByUserId_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act & Assert
        assertThrows(CustomException.class, () -> {
            skinService.getSkinsByUserId(userId, pageRequest);
        });
    }
}