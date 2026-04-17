package com.example.taskmanager.controller;

import com.example.taskmanager.exception.ValidationException;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.response.AuthResponse;
import com.example.taskmanager.model.request.LoginRequest;
import com.example.taskmanager.model.request.RegisterRequest;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Регистрация пользователя",
            description = """
                    Создаёт нового пользователя и возвращает JWT токен.
                    Доступ открыт для всех (не требует авторизации).
                    
                    Требования:
                    • email должен быть уникальным
                    • пароль должен соответствовать правилам валидации
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные или email уже существует"
                    )
            }
    )
    @PostMapping("/register")
    public AuthResponse register(
            @Parameter(description = "Данные для регистрации пользователя")
            @Validated @RequestBody RegisterRequest request) {

        if (userService.existsByEmail(request.getEmail())) {

            throw new ValidationException("Email already exists");
        }
        String token = userService.registerUser(userMapper.toDto(request));
        return new AuthResponse(token);
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = """
                    Выполняет вход по email и паролю.
                    Возвращает JWT токен при успешной аутентификации.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная авторизация",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверный email или пароль"
                    )
            }
    )
    @PostMapping("/login")
    public AuthResponse login(
            @Parameter(description = "Данные для входа пользователя")
            @Validated @RequestBody LoginRequest request) {

        String token = userService.getLogin(userMapper.toDto(request));

        return new AuthResponse(token);
    }
}

