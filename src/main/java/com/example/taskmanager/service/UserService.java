package com.example.taskmanager.service;

import com.example.taskmanager.model.dto.user.AuthUserDto;
import com.example.taskmanager.model.dto.user.RegisterUserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserService {


    boolean existsByEmail(@NotBlank @Email String email);

    String registerUser(RegisterUserDto registerUserDto);

    String getLogin(AuthUserDto authUserDto);
}
