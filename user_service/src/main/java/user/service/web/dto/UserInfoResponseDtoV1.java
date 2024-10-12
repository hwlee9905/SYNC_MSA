package user.service.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponseDtoV1 {
    private String username;
    private String nickname;
    private String position;
    private Long userId;
}
