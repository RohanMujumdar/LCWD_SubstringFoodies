package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.FoodType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodItemsDto {

    private Long id;
    private String name;
    private String description;
    private int price;
    private boolean isAvailable;
    private FoodType foodType = FoodType.VEG;
    private String imageUrl;
    private LocalDateTime localDateTime;
    private int discountAmount;
    private RestaurantDto restaurantDto;
}
