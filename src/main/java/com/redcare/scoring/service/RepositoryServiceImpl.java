package com.redcare.scoring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.redcare.scoring.client.GithubServiceClient;
import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.model.GithubApiResponse;
import com.redcare.scoring.util.JsonUtils;
import com.redcare.scoring.util.ScoreUtil;

@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final GithubServiceClient githubServiceClient;

    public RepositoryServiceImpl(final GithubServiceClient githubServiceClient) {
        this.githubServiceClient = githubServiceClient;
    }

    public List<RepositoryDto> getScoreForFetchedRepositories(final String createdAfter,
                                                              final String language,
                                                              int itemsPerPage,
                                                              int page) {
        final GithubApiResponse response = githubServiceClient.getSearchRepositories(createdAfter, language, itemsPerPage, page);

        LOGGER.info("Search repositories response body: {}", JsonUtils.asJson(response));
        return ScoreUtil.calculatePopularityScore(response.getItems());
    }
}


