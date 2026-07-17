package com.logiq.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_solutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "root_cause", columnDefinition = "TEXT", nullable = false)
    private String rootCause;

    @Lob
    @Column(name = "suggested_fix", columnDefinition = "TEXT", nullable = false)
    private String suggestedFix;

    @Lob
    @Column(name = "code_snippet", columnDefinition = "TEXT")
    private String codeSnippet;

    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false, unique = true)
    @JsonIgnore
    @ToString.Exclude
    private Log log;
}