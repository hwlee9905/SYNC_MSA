package user.service.web.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoResponseDto {
    Long userId;
    Long projectId;
    int isManager;
}
