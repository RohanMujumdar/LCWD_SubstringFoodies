package com.substring.foodies.dto;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressDto {

    private String id;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;
}
