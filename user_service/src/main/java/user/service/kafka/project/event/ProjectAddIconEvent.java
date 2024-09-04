package user.service.kafka.project.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import user.service.web.dto.project.request.AddProjectIconDto;

@AllArgsConstructor
@Getter
public class ProjectAddIconEvent {
	private AddProjectIconDto addProjectIconDto;
    private String userId;
}
