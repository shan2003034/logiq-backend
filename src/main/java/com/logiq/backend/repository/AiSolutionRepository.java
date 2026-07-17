package com.logiq.backend.repository;

import com.logiq.backend.model.AiSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiSolutionRepository extends JpaRepository<AiSolution, Long> {

    Optional<AiSolution> findByLogId(Long logId);
}