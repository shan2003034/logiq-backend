package com.logiq.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logiq.backend.model.AiSolution;
import com.logiq.backend.model.Log;
import com.logiq.backend.repository.AiSolutionRepository;
import com.logiq.backend.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final LogRepository logRepository;
    private final AiSolutionRepository aiSolutionRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiSolution analyzeLog(Long logId, boolean forceReanalyze) {
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        Optional<AiSolution> existingSolution = aiSolutionRepository.findByLogId(logId);

        if (!forceReanalyze && existingSolution.isPresent()) {
            return existingSolution.get();
        }

        String prompt = "Analyze the following Java error log. Return ONLY a valid JSON object without any markdown tags or code blocks. The JSON must have exactly these keys: 'cause' (brief root cause), 'fix' (step-by-step fix), and 'code' (code snippet to fix it). Error Message: "
                + log.getMessage() + " | Stack Trace: " + log.getStackTrace();

        String url = "https://api.groq.com/openai/v1/chat/completions";
        try {

            java.util.Map<String, Object> message = java.util.Map.of(
                    "role", "user",
                    "content", prompt
            );
            java.util.Map<String, Object> requestBodyMap = java.util.Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", java.util.List.of(message),
                    "temperature", 0.3
            );


            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey); // Groq: Authorization: Bearer <key>
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);


            String response = restTemplate.postForObject(url, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(response);
            String aiResponseText = rootNode.path("choices").get(0).path("message").path("content").asText();

            aiResponseText = aiResponseText.replace("```json", "").replace("```", "").trim();
            JsonNode aiJson = objectMapper.readTree(aiResponseText);

            AiSolution solution = existingSolution.orElse(new AiSolution());
            solution.setLog(log);
            solution.setRootCause(aiJson.path("cause").asText());
            solution.setSuggestedFix(aiJson.path("fix").asText());
            solution.setCodeSnippet(aiJson.path("code").asText());

            return aiSolutionRepository.save(solution);

        } catch (Exception e) {

            System.err.println("Groq API Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to analyze with AI: " + e.getMessage());
        }
    }

    public Optional<AiSolution> getSolutionFromDb(Long logId) {
        return aiSolutionRepository.findByLogId(logId);
    }
}