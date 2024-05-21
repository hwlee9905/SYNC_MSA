package com.simple.book.domain.member.entity;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.user.util.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "is_manager")
    private boolean isManager;
}
