package com.substring.foodies.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodCategoryDto {

    private String id;
    private String name;
    private String description;
    private List<FoodSubCategoryResponseDto> subCategories = new ArrayList<>();
}
