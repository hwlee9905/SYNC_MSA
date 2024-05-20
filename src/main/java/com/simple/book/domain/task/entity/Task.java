package com.simple.book.domain.task.entity;

import com.simple.book.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@jakarta.persistence.Entity
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    private Long id;

    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
