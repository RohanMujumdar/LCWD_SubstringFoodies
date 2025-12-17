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

    private String id;
    private LocalDateTime createdAt;
    private String creator;
    private List<CartItemsDto> cartItems;

}
