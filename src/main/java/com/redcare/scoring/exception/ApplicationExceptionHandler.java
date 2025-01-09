package com.redcare.scoring.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred.";
    private static final String ERROR_MESSAGE_KEY = "error";

    @ExceptionHandler(GithubServiceException.class)
    public ResponseEntity<Map<String, String>> handleGithubServiceException(GithubServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(ERROR_MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(LimitExceededGithubServiceException.class)
    public ResponseEntity<Map<String, String>> handleLimitExceededException(LimitExceededGithubServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of(ERROR_MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedGithubServiceException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedGithubServiceException(UnauthorizedGithubServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(ERROR_MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleDefaultException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(ERROR_MESSAGE_KEY, DEFAULT_ERROR_MESSAGE));
    }
}

