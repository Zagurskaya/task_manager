package com.example.taskmanager.security;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import com.example.taskmanager.utils.ErrorWriterUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        ErrorWriterUtil.write(response, SC_FORBIDDEN, "Forbidden", ex.getMessage());
    }
}