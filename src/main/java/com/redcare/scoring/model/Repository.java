package com.redcare.scoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    private String name;
    private Owner owner;
    @JsonProperty("stargazers_count")
    private int stargazersCount;
    @JsonProperty("forks_count")
    private int forksCount;
    @JsonProperty("updated_at")
    private String updatedAt;
}
