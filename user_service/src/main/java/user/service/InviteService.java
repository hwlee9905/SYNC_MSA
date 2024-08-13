package user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import user.service.entity.Invite;
import user.service.global.exception.LinkCannotBeSavedException;
import user.service.kafka.invite.event.UserAddToProjectLinkEvent;
import user.service.repository.InviteRepository;

@Service
@RequiredArgsConstructor
public class InviteService {
	private final InviteRepository inviteRepository;
	
	@Transactional(rollbackFor = { Exception.class })
	public void createLink(UserAddToProjectLinkEvent even) {
		UUID uuid = UUID.randomUUID();
		if (inviteRepository.existsByToken(uuid)) {
			createLink(even);
		}
		String url = "https://www.sync-team.co.kr/project/invite/" + uuid.toString();
		Invite entity = new Invite();
		entity.createLink(url, even.getProjectId(), uuid);
		try {
			inviteRepository.saveAndFlush(entity);
		} catch (Exception e) {
			throw new LinkCannotBeSavedException(e.getMessage());
		}
	}
}
