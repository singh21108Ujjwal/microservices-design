package com.sample.user_management.service;

import com.sample.user_management.dto.CreateUserRequestDto;
import com.sample.user_management.dto.CreateUserResponseDto;
import com.sample.user_management.dto.UserDto;

public interface UserService {
    CreateUserResponseDto createUser(UserDto userDto);
}
