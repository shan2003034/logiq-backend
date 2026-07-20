package com.logiq.backend.controller;

import com.logiq.backend.dto.AddCollaboratorDto;
import com.logiq.backend.model.ProjectCollaborator;
import com.logiq.backend.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/collaborators")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<?> addCollaborator(@PathVariable Long projectId, @RequestBody AddCollaboratorDto dto) {
        try {
            String message = collaboratorService.addCollaborator(projectId, dto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectCollaborator>> getCollaborators(@PathVariable Long projectId) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorsByProject(projectId));
    }
}