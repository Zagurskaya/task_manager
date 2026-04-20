package com.example.taskmanager.controller;

import com.example.taskmanager.model.request.user.AuthRequest;
import com.example.taskmanager.model.request.user.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class AuthControllerTest {

    private static final String API_REGISTER = "/api/auth/register";
    private static final String API_LOGIN = "/api/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void register_success() throws Exception {

        String userName = "user";
        String email = "user@mail.com";
        String password = "password";

        RegisterRequest request = RegisterRequest.builder()
                .username(userName)
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post(API_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("token");
    }

    @Test
    void login_success() throws Exception {

        String userName = "user_login";
        String email = "login@mail.com";
        String password = "password";

        //регистрируем пользователя
        RegisterRequest register = RegisterRequest.builder()
                .username(userName)
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post(API_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        //логинимся
        AuthRequest login = AuthRequest.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post(API_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("token");
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {

        String userName = "user";
        String email = "wrong@mail.com";
        String password = "password";

        // регистрируем
        RegisterRequest register = RegisterRequest.builder()
                .username(userName)
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post(API_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // неправильный пароль
        AuthRequest login = AuthRequest.builder()
                .email(email)
                .password("badpass")
                .build();

        mockMvc.perform(post(API_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
