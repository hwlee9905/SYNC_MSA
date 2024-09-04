package project.service.kafka.event;

import lombok.Data;

@Data
public class ProjectAddIconEvent {
	private String thumbnail;
	private String userId;
}
