package com.logiq.backend.repository;

import com.logiq.backend.model.Project;
import com.logiq.backend.model.ProjectCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProjectCollaboratorRepository extends JpaRepository<ProjectCollaborator, Long> {
    List<ProjectCollaborator> findByProjectId(Long projectId);
    Optional<ProjectCollaborator> findByProjectIdAndUserId(Long projectId, Long userId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
    @Query("SELECT pc.project FROM ProjectCollaborator pc WHERE pc.user.email = :email")
    List<Project> findSharedProjectsByUserEmail(@Param("email") String email);
}

