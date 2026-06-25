package com.healthcare.auth_service.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.healthcare.auth_service.dto.response.ApiErrorResponse;
import com.healthcare.auth_service.dto.response.ValidationErrorResponse;
import com.healthcare.auth_service.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    ResponseEntity<ApiErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException ex) {

        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        ValidationErrorResponse response = new ValidationErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Throwable cause = ex.getMostSpecificCause();

        if(cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType().equals(Role.class)) {
            ApiErrorResponse response = new ApiErrorResponse(
                    OffsetDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid role. Supported roles = " + Arrays.toString(Role.values())
            );

            return ResponseEntity.badRequest().body(response);
        }

        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request payload"
        );

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {

        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
