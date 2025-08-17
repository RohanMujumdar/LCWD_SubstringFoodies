package com.substring.foodies.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerifyObject {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

}
