package com.logiq.backend.controller;

import com.logiq.backend.dto.FrameworkResponse;
import com.logiq.backend.repository.FrameworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/frameworks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FrameworkController {

    private final FrameworkRepository frameworkRepository;

    @GetMapping
    public ResponseEntity<List<FrameworkResponse>> getAllFrameworks() {

        List<FrameworkResponse> frameworks = frameworkRepository.findAll().stream()
                .map(f -> new FrameworkResponse(f.getId(), f.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(frameworks);
    }
}