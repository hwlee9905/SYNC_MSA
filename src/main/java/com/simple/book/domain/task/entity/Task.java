package com.simple.book.domain.task.entity;

import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@jakarta.persistence.Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Long id;

    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    //순환참조
    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> subTasks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "parentTaskId")
    private Task parentTask;

}
