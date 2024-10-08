package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class DeleteFromMemberFromTaskEvent {
    private Long taskId;
    private Long userId;
}
