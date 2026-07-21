package com.logiq.backend.controller;

import com.logiq.backend.dto.UpdateFrameworkRequest;
import com.logiq.backend.dto.UpdateProjectNameRequest;
import com.logiq.backend.dto.UpdateRoleRequest;
import com.logiq.backend.service.ProjectSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/settings")
@RequiredArgsConstructor
public class ProjectSettingsController {

    private final ProjectSettingsService projectSettingsService;

    @PutMapping("/name")
    public ResponseEntity<?> updateName(@PathVariable Long projectId,
                                        @RequestBody UpdateProjectNameRequest request,
                                        Authentication authentication) {
        projectSettingsService.updateProjectName(projectId, request, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Project name updated successfully"));
    }

    @PutMapping("/framework")
    public ResponseEntity<?> updateFramework(@PathVariable Long projectId,
                                             @RequestBody UpdateFrameworkRequest request,
                                             Authentication authentication) {
        projectSettingsService.updateProjectFramework(projectId, request, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Framework updated successfully"));
    }

    @PostMapping("/api-key/regenerate")
    public ResponseEntity<?> regenerateApiKey(@PathVariable Long projectId,
                                              Authentication authentication) {
        String newKey = projectSettingsService.regenerateApiKey(projectId, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "API Key regenerated", "newApiKey", newKey));
    }

    // වෙනස් කළ කොටස: collaboratorId වෙනුවට userId භාවිතා කිරීම
    @PutMapping("/collaborators/{userId}/role")
    public ResponseEntity<?> updateCollaboratorRole(@PathVariable Long projectId,
                                                    @PathVariable Long userId,
                                                    @RequestBody UpdateRoleRequest request,
                                                    Authentication authentication) {
        projectSettingsService.updateCollaboratorRole(projectId, userId, request, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Collaborator role updated"));
    }

    // වෙනස් කළ කොටස: collaboratorId වෙනුවට userId භාවිතා කිරීම
    @DeleteMapping("/collaborators/{userId}")
    public ResponseEntity<?> removeCollaborator(@PathVariable Long projectId,
                                                @PathVariable Long userId,
                                                Authentication authentication) {
        projectSettingsService.removeCollaborator(projectId, userId, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Collaborator removed successfully"));
    }

    @DeleteMapping("/logs/clear")
    public ResponseEntity<?> clearLogs(@PathVariable Long projectId,
                                       Authentication authentication) {
        projectSettingsService.clearAllLogs(projectId, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "All logs cleared successfully"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId,
                                           Authentication authentication) {
        projectSettingsService.deleteProject(projectId, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }
}