package user.service.kafka.project.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectAddImgEvent {
	private byte[] thumbnail;
    private String userId;
}
