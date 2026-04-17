package com.example.taskmanager.service;

import com.example.taskmanager.model.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserService {


    boolean existsByEmail(@NotBlank @Email String email);

    String registerUser(UserDto userDto);

    String getLogin(UserDto dto);
}
