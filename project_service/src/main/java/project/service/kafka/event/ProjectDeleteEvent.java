package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectDeleteEvent {
    private final Long projectId;
    private final String userId;
}

