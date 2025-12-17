package com.substring.foodies.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemsDto {

    private String id;
    private Long foodItemsId;
    private int quantity;
    private String cartId;
}
