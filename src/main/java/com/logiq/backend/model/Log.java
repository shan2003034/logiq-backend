package com.logiq.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String message;

    @Lob 
    @Column(name = "stack_trace", columnDefinition = "LONGTEXT")
    private String stackTrace;

    @Column(length = 45)
    private String environment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levels_id", nullable = false)
    private Level level;


    @OneToOne(mappedBy = "log", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AiSolution aiSolution;
}
