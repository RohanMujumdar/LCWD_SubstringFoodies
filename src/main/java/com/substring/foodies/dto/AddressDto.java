package com.substring.foodies.dto;
import com.substring.foodies.dto.enums.AddressType;
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
    private AddressType addressType;
}
