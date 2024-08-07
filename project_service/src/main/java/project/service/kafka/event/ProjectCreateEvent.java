package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.CreateProjectRequestDto;

@Setter
@Getter
public class ProjectCreateEvent {
    private CreateProjectRequestDto projectCreateRequestDto;
    private String userId;

}
