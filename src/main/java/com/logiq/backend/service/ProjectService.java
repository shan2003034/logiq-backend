package com.logiq.backend.service;

import com.logiq.backend.dto.ProjectCreateRequest;
import com.logiq.backend.dto.ProjectResponse;
import com.logiq.backend.model.Framework;
import com.logiq.backend.model.Project;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.FrameworkRepository;
import com.logiq.backend.repository.ProjectCollaboratorRepository;
import com.logiq.backend.repository.ProjectRepository;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FrameworkRepository frameworkRepository;
    private final ProjectCollaboratorRepository projectCollaboratorRepository;

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

        return mapToProjectResponse(savedProject);
    }

    public List<ProjectResponse> getUserProjects(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByUserId(user.getId());

        return projects.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to this project");
        }

        return mapToProjectResponse(project);
    }

    // යාවත්කාලීන කළ අලුත් Method එක
    public List<ProjectResponse> getSharedProjectsForUser(String userEmail) {
        // 1. අදාළ User ට Share කරපු Projects ටික Database එකෙන් ගන්නවා
        List<Project> sharedProjects = projectCollaboratorRepository.findSharedProjectsByUserEmail(userEmail);

        // 2. දැනටමත් තියෙන mapToProjectResponse හරහා DTO එකට Convert කරනවා
        return sharedProjects.stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    // DTO Mapping Method එක (වෙනසක් කර නැත)
    private ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .techStack(project.getFramework().getName())
                .apiKey(project.getApiKey())
                .totalLogs(0)
                .errorsToday(0)
                .lastActive("Just now")
                .build();
    }
}