package com.redcare.scoring.exception;

public class UnauthorizedGithubServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedGithubServiceException(String message) {
        super(message);
    }
}
