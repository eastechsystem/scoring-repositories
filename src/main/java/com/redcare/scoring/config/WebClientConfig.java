package com.redcare.scoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {
    private static final String AUTHORIZATION_TOKEN_BUILDER = "Bearer %s";

    @Value("${github.developer.api.url}")
    private String githubApiRootUrl;

    @Value("${github.developer.api.token}")
    private String githubApiToken;

    @Value("${github.developer.api.content-type}")
    private String githubApiContentType;

    @Bean
    public RestTemplate githubWebServiceConfig(RestTemplateBuilder builder) {
        return builder
                .rootUri(githubApiRootUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, String.format(AUTHORIZATION_TOKEN_BUILDER, githubApiToken))
                .defaultHeader(HttpHeaders.ACCEPT, githubApiContentType)
                .build();
    }
}
