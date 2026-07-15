package com.logiq.backend.controller;

import com.logiq.backend.dto.ProjectCreateRequest;
import com.logiq.backend.dto.ProjectResponse;
import com.logiq.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {

    private final ProjectService projectService;


    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            Principal principal
    ) {
        ProjectResponse newProject = projectService.createProject(request, principal.getName());
        return ResponseEntity.ok(newProject);
    }


    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects(Principal principal) {
        List<ProjectResponse> projects = projectService.getUserProjects(principal.getName());
        return ResponseEntity.ok(projects);
    }
}