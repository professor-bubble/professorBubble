package com.bubble.buubleforprofessor.skin.service;

import com.bubble.buubleforprofessor.skin.dto.SkinResponseDto;
import com.bubble.buubleforprofessor.skin.entity.Skin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SkinService {
    Page<SkinResponseDto> getSkinsByUserId(UUID userId, Pageable pageable);
    void modifySkinStatus(UUID userId, int skinId);
}
