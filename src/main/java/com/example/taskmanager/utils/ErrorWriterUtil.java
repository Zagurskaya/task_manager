package com.example.taskmanager.utils;

import com.example.taskmanager.model.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorWriterUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void write(HttpServletResponse response,
                             int status,
                             String error,
                             String message) throws IOException {

        ErrorResponse body =
                ErrorResponse.builder()
                        .error(error)
                        .message(message)
                        .code(status).build();

        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), body);
    }
}
