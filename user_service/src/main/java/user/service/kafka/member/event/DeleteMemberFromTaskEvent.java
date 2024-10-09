package user.service.kafka.member.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class DeleteMemberFromTaskEvent{
    private Long taskId;
    private Long userId;
}
