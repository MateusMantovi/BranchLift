package com.branchlift.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "environments")
@Data
@NoArgsConstructor
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @Column(nullable = false)
    private String gitBranch;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String accessUrl;

    @Column(nullable = true)
    private Integer allocatedPort;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String buildLog;

    @Column(nullable = true) // Adicione esta linha
    private String directoryPath; // Adicione esta linha

    public Environment(Project project, String gitBranch, String createdBy) {
        this.project = project;
        this.gitBranch = gitBranch;
        this.status = "PROVISIONING";
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
}