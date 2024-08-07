package project.service.kafka.event;

import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.UpdateTaskRequestDto;

@Setter
@Getter
public class TaskUpdateEvent {
    UpdateTaskRequestDto updateTaskRequestDto;
}
