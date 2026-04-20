package com.example.taskmanager.security;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.example.taskmanager.utils.ErrorWriterUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorWriterUtil.write(response, SC_UNAUTHORIZED, "Unauthorized", authException.getMessage());
    }
}