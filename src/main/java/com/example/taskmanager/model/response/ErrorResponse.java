package com.example.taskmanager.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String error;

    private String message;

    private int code;
}
