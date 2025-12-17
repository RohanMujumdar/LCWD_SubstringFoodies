package com.substring.foodies.dto;

import com.substring.foodies.entity.FoodCategory;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodSubCategoryDto {

    private String id;
    private String name;
    private FoodCategory category;
}
