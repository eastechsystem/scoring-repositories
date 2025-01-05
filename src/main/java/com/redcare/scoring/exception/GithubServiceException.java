package com.redcare.scoring.exception;

public class GithubServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GithubServiceException(String message) {
        super(message);
    }
}
