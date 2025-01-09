package com.redcare.scoring.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.redcare.scoring.AbstractTest;

@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionHandlerTest extends AbstractTest {
    private final ApplicationExceptionHandler globalExceptionHandler = new ApplicationExceptionHandler();

    @Test
    void handleGitHubApiException_ShouldReturnBadGateway() {
        var exception = new GithubServiceException(BAD_GATEWAY_ERROR_MESSAGE);
        var response = globalExceptionHandler.handleGithubServiceException(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals(BAD_GATEWAY_ERROR_MESSAGE, response.getBody().get(ERROR_MESSAGE_KEY));
    }

    @Test
    void handleRateLimitException_ShouldReturnTooManyRequests() {
        var exception = new LimitExceededException(TOO_MANY_REQUESTS_ERROR_MESSAGE);
        var response = globalExceptionHandler.handleLimitExceededException(exception);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(TOO_MANY_REQUESTS_ERROR_MESSAGE, response.getBody().get(ERROR_MESSAGE_KEY));
    }

    @Test
    void handleUnauthorizedGithubServiceException_ShouldReturnTooManyRequests() {
        var exception = new UnauthorizedGithubServiceException(UN_AUTHORIZED_REQUESTS_ERROR_MESSAGE);
        var response = globalExceptionHandler.handleUnauthorizedGithubServiceException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(UN_AUTHORIZED_REQUESTS_ERROR_MESSAGE, response.getBody().get(ERROR_MESSAGE_KEY));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError1() {
        var errMessage = "Unexpected error";
        var exception = new Exception(errMessage);
        var response = globalExceptionHandler.handleDefaultException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(UNEXPECTED_ERROR_MESSAGE, response.getBody().get(ERROR_MESSAGE_KEY));
    }
}
