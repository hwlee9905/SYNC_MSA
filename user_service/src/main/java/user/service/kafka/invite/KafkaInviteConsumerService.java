package user.service.kafka.invite;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import user.service.InviteService;
import user.service.kafka.invite.event.UserAddToProjectLinkEvent;

@Component
@RequiredArgsConstructor
public class KafkaInviteConsumerService {
	private final InviteService inviteService;
	private static final String TOPIC = "link-add-to-project-topic";

//	@KafkaListener(topics = TOPIC, groupId = "project_create_group", containerFactory = "kafkaSendAddMemberToProjectEventListenerContainerFactory")
//	public void createLink(UserAddToProjectLinkEvent event) {
//		inviteService.createLink(event);
//	}
	@KafkaListener(topics = TOPIC, groupId = "link-add-to-project-group", containerFactory = "kafkaSendAddMemberToProjectEventListenerContainerFactory")
	public void createLink(UserAddToProjectLinkEvent event) {
//		inviteService.createLink(event);
		System.out.println("★★★★message: " + event);
	}
	
}
