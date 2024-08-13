package user.service.kafka.invite.event;

import lombok.Data;

@Data
public class UserAddToProjectLinkEvent {
	private Long projectId;
}
