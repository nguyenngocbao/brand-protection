package com.project.brandprotection.controllers;

import com.project.brandprotection.dtos.responses.ApiErrorResponse;
import com.project.brandprotection.exceptions.BadRequestException;
import com.project.brandprotection.exceptions.DuplicateException;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.exceptions.TokenRefreshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionController {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(ResourceNotFoundException e) {
        logger.error("ResourceNotFoundException", e);
        return ResponseEntity.status(NOT_FOUND).body(new ApiErrorResponse(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenRefreshException(TokenRefreshException e) {
        logger.error("TokenRefreshException", e);
        return ResponseEntity.status(FORBIDDEN).body(new ApiErrorResponse(FORBIDDEN.value(), e.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        e.getBindingResult()
                .getGlobalErrors() //Global errors are not associated with a specific field but are related to the entire object being validated.
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        String message = "Validation of request failed: %s".formatted(String.join(", ", errors));
        return ResponseEntity.status(BAD_REQUEST).body(new ApiErrorResponse(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        logger.error("BadRequestException", e);
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ApiErrorResponse(BAD_REQUEST.value(), e.getMessage()));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException() {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ApiErrorResponse(UNAUTHORIZED.value(), "Invalid username or password"));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(CONFLICT).body(new ApiErrorResponse(CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ApiErrorResponse(UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknownException(Exception e) {
        logger.error("UnhandledException", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
