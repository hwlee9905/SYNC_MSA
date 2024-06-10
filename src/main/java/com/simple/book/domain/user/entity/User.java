package com.simple.book.domain.user.entity;

import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.user.util.Address;
import com.simple.book.domain.user.util.ProfileImage;
import com.simple.book.domain.user.util.Role;
import com.simple.book.domain.user.util.Sex;
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
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String nickname;
    private String position;
    
    @Lob
    private String introduction;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authentication_id")
    private Authentication authentication;


    @ManyToMany
    @JoinTable(
            name = "user_task",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>();
}
