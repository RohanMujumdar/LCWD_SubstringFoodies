package com.substring.foodies.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddItemToCartRequest {

    private String id;
}
