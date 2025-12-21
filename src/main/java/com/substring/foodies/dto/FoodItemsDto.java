package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.FoodType;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.entity.FoodSubCategory;
import com.substring.foodies.entity.Restaurant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodItemsDto {

    private String id;
    private String name;
    private String description;
    private int price;
    private Boolean isAvailable;
    private FoodType foodType = FoodType.VEG;
    private FoodCategory foodCategory;
    private FoodSubCategory foodSubCategory;
    private String imageUrl;
    private int discountAmount;
    private double rating;
    private Set<RestaurantDto> restaurants;

}
