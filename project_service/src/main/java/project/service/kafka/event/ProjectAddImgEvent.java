package project.service.kafka.event;

import lombok.Data;

@Data
public class ProjectAddImgEvent {
	private byte[] thumbnail;
	private String userId;
}
