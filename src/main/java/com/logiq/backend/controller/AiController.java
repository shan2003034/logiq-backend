package com.logiq.backend.controller;

import com.logiq.backend.model.AiSolution;
import com.logiq.backend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiController {

    private final AiService aiService;


    @GetMapping("/solution/{logId}")
    public ResponseEntity<?> getAiSolution(@PathVariable Long logId) {
        return aiService.getSolutionFromDb(logId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    
    @PostMapping("/analyze/{logId}")
    public ResponseEntity<?> generateAiSolution(
            @PathVariable Long logId,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            AiSolution solution = aiService.analyzeLog(logId, force);
            return ResponseEntity.ok(solution);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating AI solution: " + e.getMessage());
        }
    }
}