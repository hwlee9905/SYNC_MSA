package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.MemberMappingToTaskRequestDto;

import java.util.List;

@Setter
@Getter
public class UserAddToTaskEvent {
    private List<Long> userIds;
    private Long taskId;
}
