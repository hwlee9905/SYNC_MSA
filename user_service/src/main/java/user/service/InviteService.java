package user.service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import user.service.entity.Invite;
import user.service.global.advice.SuccessResponse;
import user.service.global.exception.LinkCannotBeSavedException;
import user.service.global.exception.ProjectNotFoundException;
import user.service.global.exception.UnableToSendEmailException;
import user.service.kafka.invite.event.UserAddToProjectLinkEvent;
import user.service.repository.InviteRepository;
import user.service.web.dto.invite.request.SendLinkRequestDto;

@Service
@RequiredArgsConstructor
public class InviteService {
	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	private final InviteRepository inviteRepository;
	
	@Transactional(rollbackFor = { Exception.class })
	public void createLink(UserAddToProjectLinkEvent event) {
		UUID uuid = UUID.randomUUID();
		if (inviteRepository.existsByToken(uuid)) {
			createLink(event);
		}
		String url = "https://www.sync-team.co.kr/project/invite/" + uuid.toString();
//		Invite entity = new Invite();
//		entity.createLink(url, even.getProjectId(), uuid);
		
		Invite entity = Invite.builder()
				.url(url)
				.projectId(event.getProjectId())
				.token(uuid)
				.build();
		try {
			inviteRepository.saveAndFlush(entity);
		} catch (Exception e) {
			throw new LinkCannotBeSavedException(e.getMessage());
		}
	}
	
	public SuccessResponse getLink(long projectId) {
		Optional<Invite> inviteInfo = inviteRepository.findByProjectId(projectId);
		if (inviteInfo.isPresent()) {
			return SuccessResponse.builder().data(Collections.singletonMap("link", inviteInfo.get().getUrl())).build();
		} else {
			throw new ProjectNotFoundException();
		}
	}
	
	public SuccessResponse sendEmailLink(SendLinkRequestDto body) {
		String projectUrl;
		String projectName;
		Optional<Invite> inviteInfo = inviteRepository.findByProjectId(body.getProjectId());
		if (inviteInfo.isPresent()) {
			Invite entity = inviteInfo.get();
			projectUrl = entity.getUrl();
			projectName = body.getTitle();
		} else {
			throw new ProjectNotFoundException();
		}

		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			Context context = new Context();
			context.setVariable("url", projectUrl);
			context.setVariable("name", projectName);

			String htmlContent = templateEngine.process("email/email_template.html", context);

			helper.setTo(body.getEmail());
			helper.setFrom("sync@sync-team.co.kr");
			helper.setSubject("[sync] '" + projectName + "'의 초대 입니다!");
			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new UnableToSendEmailException(e.getMessage());
		}

		return SuccessResponse.builder().message("전송 완료").build();
	}
}