package project.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project")
public class Project{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long id;
    
    @Column(name="title", length = 255, nullable = false)
    private String title;
    
    @Column(name="sub_title", length = 255, nullable = false)
    private String subTitle;
    
    @Column(name="description", length = 255, nullable = false)
    private String description;
    
    @Column(name = "start_date", nullable = true)
    private Date startDate;
    
    @Column(name = "end_date", nullable = true)
    private Date endDate;
    
    @ColumnDefault("0")
    @Column(name = "child_count")
    private Integer childCount;
    
    @ColumnDefault("0")
    @Column(name = "child_complete_count")
    private Integer childCompleteCount;
    
    // Add this for the one-to-many relationship with Task
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}
