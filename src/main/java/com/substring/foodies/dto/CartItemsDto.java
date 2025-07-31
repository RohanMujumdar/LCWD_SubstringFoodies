package com.substring.foodies.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemsDto {

    private int id;
    private FoodItemsDto foodItemsDto;
    private int quantity;
    private CartDto cartDto;
}
