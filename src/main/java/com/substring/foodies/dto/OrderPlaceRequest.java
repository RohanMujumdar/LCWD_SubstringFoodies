package com.substring.foodies.dto;

import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.dto.enums.PaymentMode;
import com.substring.foodies.dto.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPlaceRequest {

    @NotBlank(message = "Please provide the restaurant Id.")
    private String restaurantId;

    @NotBlank(message = "Please provide the Address.")
    @Valid
    private AddressDto address;

    private OrderStatus status = OrderStatus.PLACED;

    private LocalDateTime orderedAt = LocalDateTime.now();

    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID;

    @NotBlank(message = "Please provide the payment mode.")
    private PaymentMode paymentMode;

    private String aboutThisOrder;
}
