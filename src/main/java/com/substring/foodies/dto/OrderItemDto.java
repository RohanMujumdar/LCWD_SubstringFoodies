package com.substring.foodies.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemDto {

    private String id;
    private OrderDto order;
    private FoodItemsMenuDto foodItems;
    private int quantity;
}
