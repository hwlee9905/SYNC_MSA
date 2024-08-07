package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IsExistProjectByMemberAddToProjectEvent {
    Long projectId;
    List<String> userIds;
}
