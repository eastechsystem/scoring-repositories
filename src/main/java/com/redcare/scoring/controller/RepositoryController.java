package com.redcare.scoring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.service.RepositoryServiceImpl;

@RestController
@RequestMapping("/v1/repositories")
public class RepositoryController {

    private final RepositoryServiceImpl repositoryService;

    public RepositoryController(RepositoryServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/score")
    public ResponseEntity<List<RepositoryDto>> getScoreForFetchedRepositories(
            @RequestParam(required = false, defaultValue = "2000-01-01") final String createdAfter,
            @RequestParam(required = false, defaultValue = "Q") final String language,
            @RequestParam(defaultValue = "10") int itemsPerPage,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(repositoryService.getScoreForFetchedRepositories(createdAfter, language, itemsPerPage, page));
    }
}

