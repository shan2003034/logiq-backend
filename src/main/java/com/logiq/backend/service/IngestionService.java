package com.logiq.backend.service;

import com.logiq.backend.dto.LogIngestRequest;
import com.logiq.backend.model.Log;
import com.logiq.backend.enums.LogLevel;
import com.logiq.backend.model.Project;
import com.logiq.backend.repository.LogRepository;
import com.logiq.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.logiq.backend.dto.HealthIngestRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void ingestLog(String apiKey, LogIngestRequest request) {


        Project project = projectRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key. Unauthorized access."));


        Log log = new Log();
        log.setProject(project);
        log.setMessage(request.getMessage());
        log.setStackTrace(request.getStackTrace());
        log.setEnvironment(request.getEnv());
        log.setOccurredAt(request.getTimestamp());
        log.setClassName(request.getClassName());
        log.setMethodName(request.getMethodName());
        log.setThreadName(request.getThreadName());


        try {
            log.setLevel(LogLevel.valueOf(request.getLevel().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.setLevel(LogLevel.INFO);
        }


        logRepository.save(log);
    }

    public void ingestHealth(String apiKey, HealthIngestRequest request) {

        Project project = projectRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key. Unauthorized access."));


        messagingTemplate.convertAndSend("/topic/health/" + project.getId(), request);
    }
}
