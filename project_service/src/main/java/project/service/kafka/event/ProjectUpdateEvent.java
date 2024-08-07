package project.service.kafka.event;

import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.UpdateProjectRequestDto;

@Setter
@Getter
public class ProjectUpdateEvent {
    private UpdateProjectRequestDto projectUpdateRequestDto;
}
