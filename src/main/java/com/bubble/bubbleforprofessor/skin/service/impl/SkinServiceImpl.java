package com.bubble.bubbleforprofessor.skin.service.impl;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.skin.dto.CategoryResponseDto;
import com.bubble.bubbleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.bubbleforprofessor.skin.entity.SkinImage;
import com.bubble.bubbleforprofessor.skin.entity.UserSkin;
import com.bubble.bubbleforprofessor.skin.repository.SkinRepository;
import com.bubble.bubbleforprofessor.skin.repository.UserSkinRepository;
import com.bubble.bubbleforprofessor.skin.service.SkinService;
import com.bubble.bubbleforprofessor.user.entity.User;
import com.bubble.bubbleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkinServiceImpl implements SkinService {

    private final UserSkinRepository userSkinRepository;
    private final UserRepository userRepository;
    private final SkinRepository skinRepository;

    @Transactional(readOnly= true)
    @Override
    public Page<SkinResponseDto> getSkinsByUserId(UUID userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        Page<UserSkin> skins= userSkinRepository.getSkinsByUserId(userId,pageable);
        return skins.map(userSkin -> {
            List<String> skinUrlList = userSkin.getSkin().getSkinImages().stream()
                    .map(SkinImage::getImageURL)
                    .collect(Collectors.toList());
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(userSkin.getSkin().getCategory().getId(),userSkin.getSkin().getCategory().getName());;
            // Skin -> SkinResponseDto 변환
            return SkinResponseDto.builder()
                    .skinId(userSkin.getSkin().getId())
                    .skinName(userSkin.getSkin().getName())
                    .price(userSkin.getSkin().getPrice())
                    .skinDescription(userSkin.getSkin().getDescription())
                    .category(categoryResponseDto)
                    .skinUrl(skinUrlList)  // SkinImage URL 리스트를 SkinResponseDto에 포함
                    .build();
        });
    }
    @Transactional
    @Override
    public void modifySkinStatus(UUID userId, int skinId) {
        UserSkin userSkin = userSkinRepository.findByUserIdAndSkinId(userId,skinId);
        if (userSkin == null) {
            throw new CustomException(ErrorCode.NON_EXISTENT_USER_SKIN);
        }
        userSkin.modifyActive(!userSkin.isActive());
        userSkinRepository.save(userSkin);
    }
}
