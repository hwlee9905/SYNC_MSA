package project.service.entity;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Invite")
public class Invite {
	@Id
	@Column(name = "url")
	private String url;
	
	@OneToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	@Column(name = "token", nullable = false)
	private UUID token;
	
	@Column(name = "start_date", nullable = false)
    private Date startDate;
	
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    
    public void createLink(String url, Project project, UUID token) {
    	this.url = url;
    	this.project = project;
    	this.token = token;
    }
}
