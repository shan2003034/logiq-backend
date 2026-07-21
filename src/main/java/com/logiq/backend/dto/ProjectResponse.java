package com.logiq.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String techStack;
    private String apiKey;
    private long totalLogs;
    private long errorsToday;
    private String lastActive;
    private String userRole;
}