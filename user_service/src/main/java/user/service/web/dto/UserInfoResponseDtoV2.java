package user.service.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserInfoResponseDtoV2 {
    private String username;
    private String nickname;
    private String position;
    private String userId;
}
