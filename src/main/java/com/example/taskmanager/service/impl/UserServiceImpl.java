package com.example.taskmanager.service.impl;

import com.example.taskmanager.enums.Role;
import com.example.taskmanager.exception.ApplicationException;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.dto.UserDto;
import com.example.taskmanager.model.entity.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import com.example.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;


    @Override
    public boolean existsByEmail(String email) {

        return userRepository.existsByEmail(email);

    }

    @Override
    @Transactional
    public String registerUser(UserDto userDto) {

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRole(Role.USER);

        User user = userRepository.save(userMapper.toEntity(userDto));

        return jwtService.generateToken(user.getEmail(), user.getRole());

    }

    @Override
    @Transactional(readOnly = true)
    public String getLogin(UserDto dto) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ApplicationException("User not found"));

        return jwtService.generateToken(user.getEmail(), user.getRole());
    }
}
