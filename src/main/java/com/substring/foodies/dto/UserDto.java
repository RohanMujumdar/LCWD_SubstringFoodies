package com.substring.foodies.dto;


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
    private String address;
    private String phoneNumber;
    private String gender;

    private LocalDateTime createdDateTime;
    private List<RoleEntityDto> roleEntityDtoList=new ArrayList<>();
}
