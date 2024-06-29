package com.simple.book.domain.task.entity;

import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.member.entity.MemberTask;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.user.entity.UserTask;
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
    @Column(name = "task_id")
    private Long id;

    private String title;
    private String description;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    //순환참조
    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> subTasks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTask> taskMembers = new ArrayList<>();
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTask> userTasks = new ArrayList<>();

}
