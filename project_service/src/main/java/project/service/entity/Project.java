package project.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project")
@DynamicInsert
@DynamicUpdate 
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
    
    @Column(name="thumbnail", length = 255, nullable = true)
    private String thumbnail;
    
    @Column(name="thumbnail_type", length = 1, nullable = false)
    private char thumbnailType;
    
    @Column(name = "start_date", nullable = true)
    private Date startDate;
    
    @Column(name = "end_date", nullable = true)
    private Date endDate;
    
    @ColumnDefault("0")
    @Column(name = "child_count", nullable = false)
    private Integer childCount;
    
    @ColumnDefault("0")
    @Column(name = "child_complete_count", nullable = false)
    private Integer childCompleteCount;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
}
