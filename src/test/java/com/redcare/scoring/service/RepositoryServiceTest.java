package com.redcare.scoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.redcare.scoring.AbstractTest;
import com.redcare.scoring.client.GithubServiceClient;
import com.redcare.scoring.util.JsonUtils;
import com.redcare.scoring.util.ScoreUtil;

class RepositoryServiceTest extends AbstractTest {

    @Mock
    private GithubServiceClient githubServiceClient;

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndScoreRepositories_Success() {
        // Arrange
        var createdAfter = "2022-01-01";
        var language = "java";
        var itemsPerPage = 10;
        var page = 1;

        // Mocking response from GithubServiceClient
        var mockGithubApiResponse = createMockGithubApiResponse();
        when(githubServiceClient.getSearchRepositories(createdAfter, language, itemsPerPage, page))
                .thenReturn(mockGithubApiResponse);

        var mockRepositoryDtoList = createMockRepositoryDtoList();

        // Mocking static utility methods
        var scoreUtilMock = mockStatic(ScoreUtil.class);

        scoreUtilMock.when(() -> ScoreUtil.calculatePopularityScore(mockGithubApiResponse.getItems()))
                .thenReturn(mockRepositoryDtoList);

        // Act
        var resultRepositoryDto = repositoryService.getScoreForFetchedRepositories(createdAfter, language, itemsPerPage,
                page);

        // Assert
        assertNotNull(resultRepositoryDto, "Result should not be null");
        assertEquals(3, resultRepositoryDto.size(), "Result should contain exactly 3 item");
        assertEquals("Repo1", resultRepositoryDto.get(0).getName(), "Repository name should match");
        assertEquals(106.85, resultRepositoryDto.get(0).getPopularityScore(), "Repository score should match");

        // Verify interactions
        verify(githubServiceClient, times(1)).getSearchRepositories(createdAfter, language, itemsPerPage, page);
        scoreUtilMock.verify(() -> ScoreUtil.calculatePopularityScore(mockGithubApiResponse.getItems()), times(1));
    }
}
