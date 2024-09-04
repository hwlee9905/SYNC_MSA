package user.service.kafka.project.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectAddIconEvent {
	private String thumbnail;
    private String userId;
}
