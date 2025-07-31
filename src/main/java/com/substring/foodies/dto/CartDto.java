package com.substring.foodies.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDto {

    private Long cartId;
    private LocalDateTime createdAt;
    private UserDto creator;
    private List<CartItemsDto> cartItems;

}
