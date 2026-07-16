package com.logiq.backend.dto;

import lombok.Data;

@Data
public class LogIngestRequest {
    private String env;
    private String level;
    private String message;
    private String timestamp;
    private String stackTrace;
    private String className;
    private String methodName;
    private String threadName;
}
