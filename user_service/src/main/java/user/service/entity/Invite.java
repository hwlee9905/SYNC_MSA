package user.service.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import user.service.global.entity.BaseEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invite")
public class Invite extends BaseEntity{
	@Id
	@Column(name = "url")
	private String url;
	
	@Column(name = "project_id", nullable = false)
	private Long projectId;
	
	@Column(name = "token", nullable = false)
	private UUID token;
    
    public void createLink(String url, Long projectId, UUID token) {
    	this.url = url;
    	this.projectId = projectId;
    	this.token = token;
    }
}
