package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.CreateTaskRequestDto;

@Getter
@Setter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
}
