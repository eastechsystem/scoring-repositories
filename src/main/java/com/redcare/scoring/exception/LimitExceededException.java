package com.redcare.scoring.exception;

public class LimitExceededException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LimitExceededException(String message) {
        super(message);
    }
}