package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAddToProjectLinkEvent {
	private Long projectId;
}
