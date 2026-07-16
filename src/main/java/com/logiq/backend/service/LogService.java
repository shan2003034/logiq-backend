package com.logiq.backend.service;

import com.logiq.backend.dto.LogResponse;
import com.logiq.backend.model.Log;
import com.logiq.backend.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;


    public List<LogResponse> getProjectLogs(Long projectId) {

        List<Log> logs = logRepository.findByProjectIdOrderByTimestampDesc(projectId);


        return logs.stream().map(log -> LogResponse.builder()
                .id(log.getId())
                .type(log.getLevel().name())
                .message(log.getMessage())
                .timestamp(log.getOccurredAt())
                .stackTrace(log.getStackTrace())
                .className(log.getClassName())
                .methodName(log.getMethodName())
                .build()
        ).collect(Collectors.toList());
    }
}