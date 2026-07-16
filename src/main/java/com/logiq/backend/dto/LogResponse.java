package com.logiq.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogResponse {
    private Long id;
    private String type;
    private String message;
    private String timestamp;
    private String stackTrace;
    private String className;
    private String methodName;
}
