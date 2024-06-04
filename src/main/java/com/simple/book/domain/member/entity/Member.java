package com.simple.book.domain.member.entity;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.user.entity.User;
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
    @Column(name = "memberId")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
    @Column(name = "isManager")
    private boolean isManager;
}
