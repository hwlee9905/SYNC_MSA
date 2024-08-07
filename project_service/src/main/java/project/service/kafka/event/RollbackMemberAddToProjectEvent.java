package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RollbackMemberAddToProjectEvent {

    Long projectId;
    List<String> userIds;
}
