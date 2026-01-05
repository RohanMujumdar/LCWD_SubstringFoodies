package com.substring.foodies.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

    @NotNull
    private String confirmPassword;
}
