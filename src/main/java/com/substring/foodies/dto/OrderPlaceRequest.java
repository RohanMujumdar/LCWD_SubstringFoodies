package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.dto.enums.PaymentMode;
import com.substring.foodies.dto.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderPlaceRequest {

    private String userId;
    private String address;
    private String restaurantId;
    private OrderStatus orderStatus = OrderStatus.PLACED;
    private LocalDateTime orderedAt = LocalDateTime.now();
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID;
    private PaymentMode paymentMode;
}
