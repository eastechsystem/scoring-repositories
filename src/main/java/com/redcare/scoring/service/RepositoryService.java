package com.redcare.scoring.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcare.scoring.dto.RepositoryDto;

public interface RepositoryService {
    static final Logger LOGGER = LoggerFactory.getLogger(RepositoryService.class);

    public List<RepositoryDto> getScoreForFetchedRepositories(final String createdAfter,
                                                              final String language,
                                                              int itemsPerPage,
                                                              int page);
}
