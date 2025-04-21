package com.bubble.bubbleforprofessor.skin.service;

import com.bubble.bubbleforprofessor.skin.dto.SkinResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SkinService {
    Page<SkinResponseDto> getSkinsByUserId(UUID userId, Pageable pageable);
    void modifySkinStatus(UUID userId, int skinId);
}
