package com.redcare.scoring.client;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpMethod.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.redcare.scoring.exception.GithubServiceException;
import com.redcare.scoring.model.GithubApiResponse;

@Component
public class GithubServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubServiceClient.class);
    private static final String SEARCH_REPOSITORIES_PATH = "/search/repositories?q=%s&sort=stars&order=desc&per_page=%d&page=%d";
    private static final String QUERY_PARAMS = "created:>%s language:%s";
    private static final String DEAFUALT_LANGUAGE_PARAMS = "Any";

    private final RestTemplate githubService;

    public GithubServiceClient(final RestTemplate restTemplate) {
        this.githubService = restTemplate;
        LOGGER.info("Setup github service client for endpoint: {}", githubService.getUriTemplateHandler());
    }

    public GithubApiResponse getSearchRepositories(final String createdAfter,
                                                   final String language,
                                                   int itemsPerPage,
                                                   int page) {
        final String operationPath = buildOperationPath(createdAfter, language, itemsPerPage, page);

        ResponseEntity<GithubApiResponse> response = null;
        try {
            response = githubService.exchange(
                    operationPath,
                    GET,
                    null,
                    GithubApiResponse.class);

            return nonNull(response) && nonNull(response.getBody()) ? response.getBody() : new GithubApiResponse();
        } catch (HttpClientErrorException e) {
            LOGGER.error("HTTP client error occured while calling github service {}", e);
            throw new GithubServiceException("HTTP client error occurred while calling github service.");
        } catch (Exception e) {
            LOGGER.error("Error occured while calling github service {}", e);
            throw e;
        }
    }

    private String buildOperationPath(final String createdAfter,
                                      final String language,
                                      int itemsPerPage,
                                      int page) {
        String query = String.format(QUERY_PARAMS
                , createdAfter
                , language != null && !language.isEmpty() ? language : DEAFUALT_LANGUAGE_PARAMS);

        final String operationPath = String.format(SEARCH_REPOSITORIES_PATH, query, itemsPerPage, page);

        LOGGER.info("Search repositories operation path: {}", operationPath);
        return operationPath;
    }
}
