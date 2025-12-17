package com.substring.foodies.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddItemToCartRequest {

    private String userId;
    private String foodItemId;
    private int quantity;
    private String restoId;
}
