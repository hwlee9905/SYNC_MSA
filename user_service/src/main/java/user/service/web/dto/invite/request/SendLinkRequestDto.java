package user.service.web.dto.invite.request;

import lombok.Data;

@Data
public class SendLinkRequestDto {
	private Long projectId;
	private String title;
	private String email;
}
