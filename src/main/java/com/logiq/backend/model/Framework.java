package com.logiq.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "framework")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Framework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "framework", fetch = FetchType.LAZY)
    private List<Project> projects;
}