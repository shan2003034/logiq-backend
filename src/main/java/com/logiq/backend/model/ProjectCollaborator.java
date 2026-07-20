package com.logiq.backend.model;

import com.logiq.backend.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_collaborators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCollaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Project එක සමඟ ඇති සම්බන්ධතාවය
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // User (Collaborator) සමඟ ඇති සම්බන්ධතාවය
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Enum එක හරහා Role එක ලබා දීම
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;

    // Database එකට සේව් වෙද්දී ස්වයංක්‍රීයව වෙලාව සටහන් වීම
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}