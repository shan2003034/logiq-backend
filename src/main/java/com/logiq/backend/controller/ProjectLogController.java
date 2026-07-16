package com.logiq.backend.controller;

import com.logiq.backend.dto.LogResponse;
import com.logiq.backend.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectLogController {

    private final LogService logService;


    @GetMapping("/{projectId}/logs")
    public ResponseEntity<List<LogResponse>> getLogsByProject(@PathVariable Long projectId) {

        List<LogResponse> logs = logService.getProjectLogs(projectId);
        return ResponseEntity.ok(logs);

    }
}