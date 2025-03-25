package com.bubble.buubleforprofessor.skin.service.impl;

import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.skin.dto.CategoryResponseDto;
import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.entity.Skin;
import com.bubble.buubleforprofessor.skin.entity.SkinImage;
import com.bubble.buubleforprofessor.skin.entity.UserSkin;
import com.bubble.buubleforprofessor.skin.repository.SkinRepository;
import com.bubble.buubleforprofessor.skin.repository.UserSkinRepository;
import com.bubble.buubleforprofessor.skin.service.SkinService;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkinServiceImpl implements SkinService {

    private final UserSkinRepository userSkinRepository;
    private final UserRepository userRepository;

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
}
