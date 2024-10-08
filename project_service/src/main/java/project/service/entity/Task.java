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
    
    @Column(name = "title_img")
    private String titleimg;
    
    @Lob
    private String description;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Column(name="thumbnail", length = 255, nullable = true)
    private String thumbnail;
    
    @Column(name="thumbnail_type", length = 1, nullable = false)
    private char thumbnailType;
    
    private int status;
    
    private int depth;
    
    @Column(name = "child_count")
    private Integer childCount = 0;
    
    @Column(name = "child_complete_count")
    private Integer childCompleteCount = 0;
    
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
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskImage> images = new ArrayList<>();
}
