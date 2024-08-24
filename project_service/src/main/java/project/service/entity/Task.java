package project.service.entity;
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
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long id;
    private String title;
    private String description;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    private int status;
    private int depth;
    @Column(name = "child_count")
    private int childCount;
    @Column(name = "child_complete_count")
    private int childCompleteCount;
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
    private List<UserTask> userTasks = new ArrayList<>();
}
