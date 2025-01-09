package com.redcare.scoring.exception;

public class LimitExceededGithubServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LimitExceededGithubServiceException(String message) {
        super(message);
    }
}