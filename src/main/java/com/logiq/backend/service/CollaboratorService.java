package com.logiq.backend.service;

import com.logiq.backend.dto.AddCollaboratorDto;
import com.logiq.backend.model.Project;
import com.logiq.backend.model.ProjectCollaborator;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.ProjectCollaboratorRepository;
import com.logiq.backend.repository.ProjectRepository;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final ProjectCollaboratorRepository collaboratorRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public String addCollaborator(Long projectId, AddCollaboratorDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User newCollaborator = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in LogIQ system. Ask them to register first."));


        if (project.getUser().getId().equals(newCollaborator.getId())) {
            throw new RuntimeException("User is already the owner of this project");
        }


        if (collaboratorRepository.existsByProjectIdAndUserId(projectId, newCollaborator.getId())) {
            throw new RuntimeException("User is already a collaborator in this project");
        }

        ProjectCollaborator collaborator = new ProjectCollaborator();
        collaborator.setProject(project);
        collaborator.setUser(newCollaborator);
        collaborator.setRole(dto.getRole());

        collaboratorRepository.save(collaborator);

        emailService.sendCollaborationInvite(
                newCollaborator.getEmail(),
                project.getName(),
                project.getUser().getFirstName(),
                dto.getRole().name(),
                project.getId()
        );

        return "Collaborator added successfully!";
    }

    public List<ProjectCollaborator> getCollaboratorsByProject(Long projectId) {
        return collaboratorRepository.findByProjectId(projectId);
    }
}