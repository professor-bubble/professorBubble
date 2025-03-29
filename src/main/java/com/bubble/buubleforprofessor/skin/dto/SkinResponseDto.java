package com.bubble.buubleforprofessor.skin.dto;

import com.bubble.buubleforprofessor.skin.entity.Category;
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
