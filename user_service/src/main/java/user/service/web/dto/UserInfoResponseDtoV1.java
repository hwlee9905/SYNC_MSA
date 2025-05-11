package user.service.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class UserInfoResponseDtoV1 {
    private String username;
    private String nickname;
    private String position;
    private Long userId;
}
