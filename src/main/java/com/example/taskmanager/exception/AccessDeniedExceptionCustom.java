package com.example.taskmanager.exception;

public class AccessDeniedExceptionCustom extends RuntimeException {

    public AccessDeniedExceptionCustom(String message) {
        super(message);
    }
}

