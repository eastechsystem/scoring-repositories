package com.redcare.scoring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.redcare.scoring.AbstractTest;
import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.service.RepositoryServiceImpl;

class RepositoryControllerTest extends AbstractTest {

    @Mock
    private RepositoryServiceImpl repositoryService;

    @InjectMocks
    private RepositoryController repositoryController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRepositoriesDefaultParams() {
        // Mock data
        List<RepositoryDto> mockRepositories = createMockRepositoryDtoList();
        when(repositoryService.getScoreForFetchedRepositories("2000-01-01", "Q", 10, 1)).thenReturn(mockRepositories);

        // Call the method
        ResponseEntity<List<RepositoryDto>> response = repositoryController.getScoreForFetchedRepositories("2000-01-01", "Q", 10, 1);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRepositories, response.getBody());

        // Verify the interaction
        verify(repositoryService, times(1)).getScoreForFetchedRepositories("2000-01-01", "Q", 10, 1);
    }

    @Test
    void testGetRepositoriesCustomParams() {
        // Mock data
        List<RepositoryDto> mockRepositories = createMockRepositoryDtoList();

        when(repositoryService.getScoreForFetchedRepositories("2023-01-01", "Java", 5, 2)).thenReturn(mockRepositories);

        // Call the method
        ResponseEntity<List<RepositoryDto>> response = repositoryController.getScoreForFetchedRepositories("2023-01-01", "Java", 5, 2);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRepositories, response.getBody());

        // Verify the interaction
        verify(repositoryService, times(1)).getScoreForFetchedRepositories("2023-01-01", "Java", 5, 2);
    }
}
