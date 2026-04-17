package com.example.taskmanager.advice;

import static com.example.taskmanager.constant.ExceptionMessageConstant.NOT_VALID_EMAIL_OR_PASSWORD;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.example.taskmanager.exception.AccessDeniedExceptionCustom;
import com.example.taskmanager.exception.ApplicationException;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.exception.ValidationException;
import java.util.LinkedHashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedExceptionCustom.class)
    public ResponseEntity<?> handleDenied(AccessDeniedExceptionCustom ex) {
        return ResponseEntity.status(FORBIDDEN).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidation(ValidationException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, Object> handleValidationParam(MethodArgumentNotValidException ex) {

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "Validation failed");

        Map<String, String> fields = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(f ->
                fields.put(f.getField(), f.getDefaultMessage())
        );
        error.put("fields", fields);
        return error;
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleBadEmailAndPassword(BadCredentialsException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(Map.of("error", NOT_VALID_EMAIL_OR_PASSWORD));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplication(ApplicationException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }
}

