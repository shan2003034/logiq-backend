package com.logiq.backend.repository;

import com.logiq.backend.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findByProjectIdOrderByTimestampDesc(Long projectId);

    void deleteByProjectId(Long projectId);

    // Project එකේ සම්පූර්ණ Logs ගණන ලබාගැනීමට
    long countByProjectId(Long projectId);

    // අද දවසේ ආපු ERROR Logs ගණන ලබාගැනීමට
    @Query("SELECT COUNT(l) FROM Log l WHERE l.project.id = :projectId AND l.level = 'ERROR' AND l.timestamp >= :startOfDay")
    long countErrorsToday(@Param("projectId") Long projectId, @Param("startOfDay") LocalDateTime startOfDay);
}