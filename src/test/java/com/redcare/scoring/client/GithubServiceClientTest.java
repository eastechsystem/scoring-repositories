package com.redcare.scoring.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.redcare.scoring.AbstractTest;
import com.redcare.scoring.exception.GithubServiceException;
import com.redcare.scoring.model.GithubApiResponse;

public class GithubServiceClientTest extends AbstractTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubServiceClient githubServiceClient;

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSearchRepositories_Success() {
        // Arrange
        var createdAfter = "2022-01-01";
        var language = "java";
        var itemsPerPage = 10;
        var page = 1;
        var mockResponse = new GithubApiResponse();

        var mockEntity = ResponseEntity.ok(mockResponse);
        when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(GithubApiResponse.class))).thenReturn(mockEntity);

        // Act
        var result = githubServiceClient.getSearchRepositories(createdAfter, language, itemsPerPage, page);

        // Assert
        assertNotNull(result);
        verify(restTemplate).exchange(urlCaptor.capture(), eq(GET), eq(null), eq(GithubApiResponse.class));
        var capturedUrl = urlCaptor.getValue();
        LOGGER.info("Captured URL: {}", capturedUrl);
    }

    @Test
    void testGetSearchRepositories_HttpClientErrorException() {
        // Arrange
        var createdAfter = "2022-01-01";
        var language = "java";
        var itemsPerPage = 10;
        var page = 1;

        when(restTemplate.exchange(eq(EXPECTED_URL), eq(GET), eq(null), eq(GithubApiResponse.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST));

        // Act & Assert
        var exception = assertThrows(GithubServiceException.class,
                () -> githubServiceClient.getSearchRepositories(createdAfter, language, itemsPerPage, page));

        assertEquals(HTTP_CLIENT_ERROR_MESSAGE, exception.getMessage());
        verify(restTemplate, times(1)).exchange(eq(EXPECTED_URL), eq(GET), eq(null), eq(GithubApiResponse.class));
    }

    @Test
    void testGetSearchRepositories_OtherException() {
        // Arrange
        var createdAfter = "2022-01-01";
        var language = "java";
        var itemsPerPage = 10;
        var page = 1;

        when(restTemplate.exchange(eq(EXPECTED_URL), eq(GET), eq(null), eq(GithubApiResponse.class)))
                .thenThrow(new RuntimeException(UNEXPECTED_ERROR_MESSAGE));

        // Act & Assert
        var exception = assertThrows(RuntimeException.class,
                () -> githubServiceClient.getSearchRepositories(createdAfter, language, itemsPerPage, page));

        assertEquals(UNEXPECTED_ERROR_MESSAGE, exception.getMessage());
        verify(restTemplate, times(1)).exchange(eq(EXPECTED_URL), eq(GET), eq(null), eq(GithubApiResponse.class));
    }
}
