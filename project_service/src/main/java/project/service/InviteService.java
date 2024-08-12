package project.service;

import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import lombok.RequiredArgsConstructor;
import project.service.entity.Invite;
import project.service.entity.Project;
import project.service.global.exception.LinkCannotBeSavedException;
import project.service.repository.InviteRepository;
import project.service.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class InviteService {
	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	private final InviteRepository inviteRepository;
	private final ProjectRepository projectRepository;

	@Transactional(rollbackFor = { Exception.class })
	public void createLink(Project project) {
		UUID uuid = UUID.randomUUID();
		if (inviteRepository.existsByToken(uuid)) {
			createLink(project);
		}
		String url = "https://www.sync-team.co.kr/project/invite/" + uuid.toString();
		Invite entity = new Invite();
		entity.createLink(url, project, uuid);
		try {
			inviteRepository.saveAndFlush(entity);
		} catch (Exception e) {
			throw new LinkCannotBeSavedException(e.getMessage());
		}
	}
}
