package com.bubble.bubbleforprofessor.skin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkinResponseDto {
    private int skinId;
    private String skinName;
    private int price;
    private String skinDescription;
    private CategoryResponseDto category;
    private List<String> skinUrl;
}
