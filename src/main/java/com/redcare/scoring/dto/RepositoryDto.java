package com.redcare.scoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDto {
    private String name;
    private String owner;
    private int stars;
    private int forks;
    private String lastUpdated;
    private double popularityScore;
}