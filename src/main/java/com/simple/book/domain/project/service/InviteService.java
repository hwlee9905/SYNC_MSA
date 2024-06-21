package com.simple.book.domain.project.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.simple.book.domain.project.dto.InviteDto;
import com.simple.book.domain.project.entity.Invite;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.InviteReposotiry;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.global.advice.ResponseMessage;
import com.simple.book.global.exception.EntityNotFoundException;
import com.simple.book.global.exception.UnknownException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteService {
	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	private final InviteReposotiry inviteReposotiry;
	private final ProjectRepository projectRepository;

	public ResponseMessage createLink(Project project) {
		UUID uuid = UUID.randomUUID();
		if (inviteReposotiry.existsByToken(uuid)) {
			createLink(project);
		}
		String url = "https://www.sync-team.co.kr/project/invite/" + uuid.toString();
		InviteDto dto = InviteDto.builder()
				.url(url)
				.project(project)
				.token(uuid)
				.build();
		try {
			inviteReposotiry.saveAndFlush(dto.toEntity());
		} catch (Exception e) { 
			throw new UnknownException(e.getMessage());
		}
		return ResponseMessage.builder().message(url).build();
	}

	public ResponseMessage getLink(long projectId) {
		Optional<Invite> inviteInfo = inviteReposotiry.findByProject(projectRepository.getReferenceById(projectId));
		if (inviteInfo.isPresent()) {
			return ResponseMessage.builder().value(inviteInfo.get().toDto().getUrl()).build();
		} else {
			throw new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다. ProjectId : " + projectId);
		}
	}

	public ResponseMessage emailLink(long projectId, String email) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			Map<String, Object> projectInfo = findProjectInfo(projectId);

			Context context = new Context();
			context.setVariable("url", projectInfo.get("url"));
			context.setVariable("name", projectInfo.get("name"));

			String htmlContent = templateEngine.process("email/email_template.html", context);
			
			helper.setTo(email);
			helper.setFrom("sync@sync-team.co.kr");
			helper.setSubject("[sync] '" + projectInfo.get("name")+ "'의 초대 입니다!");
			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownException(e.getMessage());
		}

		return ResponseMessage.builder().message("전송 완료").build();
	}
	
	private Map<String, Object> findProjectInfo(long projectId) {
		Map<String, Object> result = new HashMap<>();
		Optional<Project> project = projectRepository.findById(projectId);
		if (project.isPresent()) {
			result.put("name", project.get().getTitle());
			Optional<Invite> invite = inviteReposotiry.findByProject(project.get());
			if (invite.isPresent()) {
				result.put("url", invite.get().toDto().getUrl());
			} else {
				throw new UnknownException(null);
			}
		} else {
			throw new UnknownException(null);
		}
		return result;
	}
}
