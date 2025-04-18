package com.sample.user_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    @NotNull(message = "first name shouldn't be null")
    @Size(min = 2, message = "first name length should be more than 2 characters")
    private String firstName;

    @NotNull(message = "last name shouldn't be null")
    @Size(min = 2, message = "last name length should be more than 2 characters")
    private String lastName;

    @Email(message = "email can't be null")
    private String email;

    @NotNull(message = "password shouldn't be null")
    @Size(min = 8, max = 16, message = "password length should be in b/w 8 to 16 characters")
    private String password;
}
