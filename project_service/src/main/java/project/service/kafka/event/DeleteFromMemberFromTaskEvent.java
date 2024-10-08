package project.service.kafka.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteFromMemberFromTaskEvent {
    private Long taskId;
    private Long userId;
}
