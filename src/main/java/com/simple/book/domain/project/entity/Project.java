package com.simple.book.domain.project.entity;


import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.user.entity.User;
import com.simple.book.global.util.BaseEntity;
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
@Table(name = "project")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    private String title;
    private String description;
    @OneToMany(mappedBy = "project")
    private List<Member> members = new ArrayList<>();
}
