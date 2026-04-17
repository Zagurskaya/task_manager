package com.example.taskmanager.mapper;

import com.example.taskmanager.enums.Role;
import com.example.taskmanager.model.dto.user.AuthUserDto;
import com.example.taskmanager.model.dto.user.RegisterUserDto;
import com.example.taskmanager.model.dto.user.UserDto;
import com.example.taskmanager.model.entity.User;
import com.example.taskmanager.model.request.user.AuthRequest;
import com.example.taskmanager.model.request.user.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    User toEntity(UserDto userDto);

    @Mapping(target = "username", source = "registerUserDto.username")
    @Mapping(target = "email", source = "registerUserDto.email")
    @Mapping(target = "password", source = "registerUserDto.password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterUserDto registerUserDto, Role role);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    UserDto toDto(User user);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserDto toDto(Long userId);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    RegisterUserDto toDto(RegisterRequest request);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    AuthUserDto toDto(AuthRequest request);
}
