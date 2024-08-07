package project.service.kafka.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDeleteEvent {
    Long taskId;
}
