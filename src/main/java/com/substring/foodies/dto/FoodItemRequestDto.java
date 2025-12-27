package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.FoodType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemRequestDto {

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private int price;

    @NotNull
    private Boolean isAvailable;

    @NotNull
    private FoodType foodType;

    private String imageUrl;

    @Min(0)
    private int discountAmount;

    // 🔗 RELATION IDs ONLY
    @NotBlank
    private String foodCategoryId;

    @NotBlank
    private String foodSubCategoryId;

    @NotNull
    private Set<String> restaurantIds;
}
