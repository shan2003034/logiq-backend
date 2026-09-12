package com.logiq.backend.service;

import com.logiq.backend.dto.UpdateFrameworkRequest;
import com.logiq.backend.dto.UpdateProjectNameRequest;
import com.logiq.backend.dto.UpdateRoleRequest;
import com.logiq.backend.model.Framework;
import com.logiq.backend.model.Project;
import com.logiq.backend.model.ProjectCollaborator;
import com.logiq.backend.enums.ProjectRole;
import com.logiq.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectSettingsService {

    private final ProjectRepository projectRepository;
    private final FrameworkRepository frameworkRepository;
    private final ProjectCollaboratorRepository projectCollaboratorRepository;
    private final LogRepository logRepository;
    private final EmailService emailService;


    private Project getProjectIfOwner(Long projectId, String userEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized: Only the project owner can perform this action");
        }
        return project;
    }

    public void updateProjectName(Long projectId, UpdateProjectNameRequest request, String userEmail) {
        Project project = getProjectIfOwner(projectId, userEmail);
        project.setName(request.getName());
        projectRepository.save(project);
    }

    public void updateProjectFramework(Long projectId, UpdateFrameworkRequest request, String userEmail) {
        Project project = getProjectIfOwner(projectId, userEmail);
        Framework framework = frameworkRepository.findById(request.getFrameworkId())
                .orElseThrow(() -> new RuntimeException("Framework not found"));

        project.setFramework(framework);
        projectRepository.save(project);
    }

    public String regenerateApiKey(Long projectId, String userEmail) {
        Project project = getProjectIfOwner(projectId, userEmail);
        String newApiKey = "logiq_pk_" + UUID.randomUUID().toString().replace("-", "");
        project.setApiKey(newApiKey);
        projectRepository.save(project);
        return newApiKey;
    }


    public void updateCollaboratorRole(Long projectId, Long userId, UpdateRoleRequest request, String userEmail) {
        Project project = getProjectIfOwner(projectId, userEmail);

        ProjectCollaborator collaborator = projectCollaboratorRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        String newRole = request.getRole().toUpperCase();
        collaborator.setRole(ProjectRole.valueOf(newRole));
        projectCollaboratorRepository.save(collaborator);


        try {
            emailService.sendRoleUpdateEmail(
                    collaborator.getUser().getEmail(),
                    project.getName(),
                    newRole,
                    projectId
            );
        } catch (Exception e) {
            System.err.println("Failed to send role update email: " + e.getMessage());
        }
    }


    public void removeCollaborator(Long projectId, Long userId, String userEmail) {
        Project project = getProjectIfOwner(projectId, userEmail);

        ProjectCollaborator collaborator = projectCollaboratorRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        String collabEmail = collaborator.getUser().getEmail();
        String projectName = project.getName();

        projectCollaboratorRepository.delete(collaborator);


        try {
            emailService.sendProjectRemovalEmail(collabEmail, projectName);
        } catch (Exception e) {
            System.err.println("Failed to send removal email: " + e.getMessage());
        }
    }

    @Transactional
    public void clearAllLogs(Long projectId, String userEmail) {
        getProjectIfOwner(projectId, userEmail);
        logRepository.deleteByProjectId(projectId);
    }

    @Transactional
    public void deleteProject(Long projectId, String userEmail) {
        getProjectIfOwner(projectId, userEmail);
        projectRepository.deleteById(projectId);
    }
}