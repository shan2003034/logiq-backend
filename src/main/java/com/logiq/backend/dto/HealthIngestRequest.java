package com.logiq.backend.dto;

import lombok.Data;

@Data
public class HealthIngestRequest {
    private String env;
    private double cpuLoad;
    private long uptime;
    private double systemMemoryUsage;
    private double jvmMemoryUsage;
    private int activeThreadCount;
    private double diskSpaceUsage;
    private long gcCollectionCount;
    private long gcCollectionTime;
}
