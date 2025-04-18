package com.sample.user_management.controller;

import com.netflix.discovery.converters.Auto;
import com.sample.user_management.dto.CreateUserRequestDto;
import com.sample.user_management.dto.CreateUserResponseDto;
import com.sample.user_management.dto.UserDto;
import com.sample.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private Environment env;

    @Autowired
    private final UserService userService;

    @GetMapping("/status/check")
    public String status(){

        return "Working on port: " + env.getProperty("local.server.port");
    }

    @PostMapping()
    public ResponseEntity<CreateUserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(createUserRequest, UserDto.class);
        CreateUserResponseDto createUserResponseDto = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createUserResponseDto);

    }
}
