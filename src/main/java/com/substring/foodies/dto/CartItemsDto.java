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
    private Long foodItemsId;
    private int quantity;
    private Long cartId;
}
