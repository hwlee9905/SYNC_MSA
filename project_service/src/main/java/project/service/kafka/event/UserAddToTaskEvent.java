package project.service.kafka.event;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserAddToTaskEvent {
    private List<Long> userIds;
    private Long taskId;
}
