package com.substring.foodies.dto;


import com.substring.foodies.Utility.ValidGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignUpUserDto {

    @NotEmpty(message = "Please provide the Id.")
    private String id;

    @NotEmpty(message = "Please provide your Name.")
    @Size(min=2, max=20, message = "Name must be between 2 and 20 characters.")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one letter, one number, and one special character")
    private String password;

    @NotEmpty(message = "ConfirmPassword cannot be empty")
    private String confirmPassword;

    @ValidGender
    // We can also give our custom message.
    private String gender;

    private List<RoleEntityDto> roleEntityDtoList=new ArrayList<>();
}
