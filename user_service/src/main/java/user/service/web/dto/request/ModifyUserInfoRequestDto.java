package user.service.web.dto.request;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModifyUserInfoRequestDto {
	private String username;
	private String nickname;
	private String position;
	@Lob
	private String introduction;
}
