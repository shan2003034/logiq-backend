package com.logiq.backend.controller;

import com.logiq.backend.dto.LogIngestRequest;
import com.logiq.backend.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.logiq.backend.dto.HealthIngestRequest;

@RestController
@RequestMapping("/api/ingest")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;


    @PostMapping("/logs")
    public ResponseEntity<String> ingestLog(
            @RequestHeader(value = "x-api-key", required = true) String apiKey,
            @RequestBody LogIngestRequest request
    ) {
        try {

            ingestionService.ingestLog(apiKey, request);
            return ResponseEntity.ok("Log ingested successfully");

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process log");
        }
    }

    @PostMapping("/health")
    public ResponseEntity<String> ingestHealth(
            @RequestHeader(value = "x-api-key", required = true) String apiKey,
            @RequestBody HealthIngestRequest request
    ) {
        try {
            ingestionService.ingestHealth(apiKey, request);
            return ResponseEntity.ok("Health data broadcasted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process health data");
        }
    }
}
