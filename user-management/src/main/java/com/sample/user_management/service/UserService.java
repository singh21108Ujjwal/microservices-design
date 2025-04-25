package com.sample.user_management.service;

import com.sample.user_management.dto.CreateUserRequestDto;
import com.sample.user_management.dto.CreateUserResponseDto;
import com.sample.user_management.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    CreateUserResponseDto createUser(UserDto userDto);
    CreateUserResponseDto getUserDetailsByEmail(String email);
}
