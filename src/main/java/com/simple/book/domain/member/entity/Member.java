package com.simple.book.domain.member.entity;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "user_id",updatable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "project_id",updatable = false)
    private Project project;
    @Column(name = "is_manager")
    private boolean isManager;
    @OneToMany(mappedBy = "id.mappingMemberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskMember> taskMembers = new ArrayList<>();
}
