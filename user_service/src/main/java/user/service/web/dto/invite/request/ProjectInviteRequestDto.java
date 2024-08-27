package user.service.web.dto.invite.request;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProjectInviteRequestDto {
	@NotEmpty(message = "토큰은 필수 값 입니다.")
	private UUID Token;
}
