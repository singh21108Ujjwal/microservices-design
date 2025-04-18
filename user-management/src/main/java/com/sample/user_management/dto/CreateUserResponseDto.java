package com.sample.user_management.dto;

import lombok.Data;

@Data
public class CreateUserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
}
