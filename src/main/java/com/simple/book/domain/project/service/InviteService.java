package com.simple.book.domain.project.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.simple.book.global.exception.UnknownException;
import com.simple.book.global.util.ResponseMessage;

import jakarta.mail.internet.MimeMessage;

@Service
public class InviteService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private InviteReposotiry inviteReposotiry;

	@Autowired
	private ProjectRepository projectRepository;

	public ResponseMessage createLink(long projectId) {
		UUID uuid = UUID.randomUUID();
		if (inviteReposotiry.existsByToken(uuid)) {
			createLink(projectId);
		}
		String url = "https://www.sync-team.co.kr/project/invite/" + uuid.toString();
		InviteDto dto = InviteDto.builder()
				.url(url)
				.project(projectRepository.getReferenceById(projectId))
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
			return ResponseMessage.builder().message(inviteInfo.get().toDto().getUrl()).build();
		} else {
			throw new UnknownException(null);
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

			String htmlContent = templateEngine.process("/email/email_template.html", context);
			
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
