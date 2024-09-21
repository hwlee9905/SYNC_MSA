package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.service.dto.request.MemberRemoveRequestDto;

@AllArgsConstructor
@Getter
public class DeleteFromMemberFromTaskEvent {
    MemberRemoveRequestDto memberRemoveRequestDto;
}
