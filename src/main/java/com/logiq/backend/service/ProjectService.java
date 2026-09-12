package com.logiq.backend.service;

import com.logiq.backend.dto.ProjectCreateRequest;
import com.logiq.backend.dto.ProjectResponse;
import com.logiq.backend.dto.TeamMemberResponse;
import com.logiq.backend.model.Framework;
import com.logiq.backend.model.Project;
import com.logiq.backend.model.ProjectCollaborator;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.FrameworkRepository;
import com.logiq.backend.repository.LogRepository;
import com.logiq.backend.repository.ProjectCollaboratorRepository;
import com.logiq.backend.repository.ProjectRepository;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FrameworkRepository frameworkRepository;
    private final ProjectCollaboratorRepository projectCollaboratorRepository;


    private final LogRepository logRepository;

    public ProjectResponse createProject(ProjectCreateRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Framework framework = frameworkRepository.findById(request.getFrameworkId())
                .orElseThrow(() -> new RuntimeException("Framework not found"));

        String generatedApiKey = "logiq_pk_" + UUID.randomUUID().toString().replace("-", "");

        Project project = new Project();
        project.setName(request.getName());
        project.setApiKey(generatedApiKey);
        project.setUser(user);
        project.setFramework(framework);

        Project savedProject = projectRepository.save(project);

        return mapToProjectResponse(savedProject, "OWNER");
    }

    public List<ProjectResponse> getUserProjects(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByUserId(user.getId());

        return projects.stream()
                .map(project -> mapToProjectResponse(project, "OWNER"))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        String role;

        if (project.getUser().getId().equals(user.getId())) {
            role = "OWNER";
        } else {
            Optional<ProjectCollaborator> collaborator = projectCollaboratorRepository.findByProjectIdAndUserEmail(id, userEmail);

            if (collaborator.isPresent()) {
                role = collaborator.get().getRole().name();
            } else {
                throw new RuntimeException("Unauthorized access to this project");
            }
        }

        return mapToProjectResponse(project, role);
    }

    public List<ProjectResponse> getSharedProjectsForUser(String userEmail) {
        List<ProjectCollaborator> sharedCollaborations = projectCollaboratorRepository.findByUserEmail(userEmail);

        return sharedCollaborations.stream()
                .map(collab -> mapToProjectResponse(
                        collab.getProject(),
                        collab.getRole().name()
                ))
                .collect(Collectors.toList());
    }


    private ProjectResponse mapToProjectResponse(Project project, String role) {

        long totalLogs = logRepository.countByProjectId(project.getId());

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        long errorsToday = logRepository.countErrorsToday(project.getId(), startOfToday);


        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .techStack(project.getFramework().getName())
                .apiKey(project.getApiKey())
                .totalLogs(totalLogs)
                .errorsToday(errorsToday)
                .lastActive("Just now")
                .userRole(role)
                .build();
    }

    public List<TeamMemberResponse> getProjectTeam(Long projectId, String userEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<TeamMemberResponse> team = new ArrayList<>();

        team.add(TeamMemberResponse.builder()
                .userId(project.getUser().getId())
                .name(project.getUser().getFirstName())
                .email(project.getUser().getEmail())
                .role("OWNER")
                .build());

        List<ProjectCollaborator> collaborators = projectCollaboratorRepository.findByProjectId(projectId);

        for (ProjectCollaborator collab : collaborators) {
            team.add(TeamMemberResponse.builder()
                    .userId(collab.getUser().getId())
                    .name(collab.getUser().getFirstName())
                    .email(collab.getUser().getEmail())
                    .role(collab.getRole().name())
                    .build());
        }

        return team;
    }
}