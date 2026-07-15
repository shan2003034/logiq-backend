package com.logiq.backend.repository;

import com.logiq.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


    List<Project> findByUserId(Long userId);


    Optional<Project> findByApiKey(String apiKey);
}
