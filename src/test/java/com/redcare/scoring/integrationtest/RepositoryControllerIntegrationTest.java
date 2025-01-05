package com.redcare.scoring.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.util.JsonUtils;

import io.netty.util.internal.StringUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryControllerIntegrationTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RepositoryControllerIntegrationTest.class);
    private final static String VIRTUAL_HOST_REPOSITORY_URL = "http://localhost:%d/v1/repositories/score%s";
    private final static String REPOSITORIES_PARAMS = "?createdAfter=2023-01-01&language=Java&itemsPerPage=5&page=2";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetRepositoriesHavingNoParams() {
        // Invoke the Rest API having no parameters
        var url = String.format(VIRTUAL_HOST_REPOSITORY_URL, port, StringUtil.EMPTY_STRING);
        var response = restTemplate.getForEntity(url, RepositoryDto[].class);

        // log the response
        LOGGER.info(JsonUtils.asJson(response));

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(0); // No repositories or some default data
    }

    @Test
    void testGetRepositoriesHavingCustomParams() {
        // Invoke the Rest API having custom parameters
        var url = String.format(VIRTUAL_HOST_REPOSITORY_URL, port, REPOSITORIES_PARAMS);
        var response = restTemplate.getForEntity(url, RepositoryDto[].class);

        // log the response
        LOGGER.info(JsonUtils.asJson(response));

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(0); // No repositories or some default data
    }
}
