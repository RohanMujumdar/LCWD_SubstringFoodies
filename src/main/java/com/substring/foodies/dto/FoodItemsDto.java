package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.FoodType;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.entity.FoodSubCategory;
import com.substring.foodies.entity.Restaurant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodItemsDto {

    private String id;
    private String name;
    private String description;
    private int price;
    private boolean isAvailable;
    private FoodType foodType = FoodType.VEG;
    private FoodCategory foodCategory;
    private FoodSubCategory foodSubCategory;
    private String imageUrl;
    private LocalDateTime localDateTime;
    private int discountAmount;
    private List<RestaurantDto> restaurants;

}
