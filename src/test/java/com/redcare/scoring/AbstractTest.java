package com.redcare.scoring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.model.GithubApiResponse;
import com.redcare.scoring.model.Owner;
import com.redcare.scoring.model.Repository;

public class AbstractTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
    protected static final String ERROR_MESSAGE_KEY = "error";
    protected static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred.";
    protected static final String BAD_GATEWAY_ERROR_MESSAGE = "GitHub API error";
    protected static final String TOO_MANY_REQUESTS_ERROR_MESSAGE = "Rate limit exceeded";
    protected static final String HTTP_CLIENT_ERROR_MESSAGE = "HTTP client error occurred while calling github service.";
    protected static final String EXPECTED_URL = "/search/repositories?q=created:>2022-01-01 language:java&sort=stars&order=desc&per_page=10&page=1";

    protected GithubApiResponse createMockGithubApiResponse() {
        Repository mockItem = new Repository();
        mockItem.setName("Test Repository");

        List<Repository> repositories = new ArrayList<>();
        repositories.add(new Repository("Repo1", new Owner("testUser1"), 150, 50, "2024-12-30T12:00:00"));
        repositories.add(new Repository("Repo2", new Owner("testUser2"), 200, 80, "2023-12-15T10:00:00"));
        repositories.add(new Repository("Repo3", new Owner("testUser3"), 100, 30, "2025-01-01T09:00:00"));

        GithubApiResponse mockResponse = new GithubApiResponse();
        mockResponse.setItems(repositories);
        return mockResponse;
    }

    protected List<RepositoryDto> createMockRepositoryDtoList() {
        List<RepositoryDto> repositoriesDtoList = new ArrayList<>();
        repositoriesDtoList.add(new RepositoryDto("Repo1", "testUser1", 150, 50, "2024-12-30T12:00:00", 106.85));
        repositoriesDtoList.add(new RepositoryDto("Repo2", "testUser2", 200, 80, "2023-12-15T10:00:00", 157.14));
        repositoriesDtoList.add(new RepositoryDto("Repo3", "testUser3", 100, 30, "2025-01-01T09:00:00", 101.98));

        return repositoriesDtoList;
    }
}
