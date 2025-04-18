package com.sample.user_management.serviceImpl;

import com.sample.user_management.dto.CreateUserRequestDto;
import com.sample.user_management.dto.CreateUserResponseDto;
import com.sample.user_management.dto.UserDto;
import com.sample.user_management.model.UserEntity;
import com.sample.user_management.repository.UserRepository;
import com.sample.user_management.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public CreateUserResponseDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, CreateUserResponseDto.class);
    }
}
