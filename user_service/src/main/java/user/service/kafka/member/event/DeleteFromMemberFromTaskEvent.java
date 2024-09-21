package user.service.kafka.member.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import user.service.web.dto.member.request.MemberRemoveRequestDto;
@AllArgsConstructor
@Getter
public class DeleteFromMemberFromTaskEvent{
    MemberRemoveRequestDto memberRemoveRequestDto;
}
