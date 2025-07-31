package com.substring.foodies.dto;

import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.Order;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemDto {

    private Long id;
    private OrderDto orderDto;
    private FoodItemsDto foodItemsDto;
    private int quantity;
}
