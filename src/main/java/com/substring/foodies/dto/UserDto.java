package com.substring.foodies.dto;


import com.substring.foodies.dto.enums.Role;
import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private List<AddressDto> addressList;
    private String phoneNumber;
    private Role role = Role.ROLE_USER;
    private boolean isAvailable=true;
    private List<RestaurantDto> restaurantList=new ArrayList<>();
    private LocalDate createdDate;
    private boolean isEnabled=true;

}
