package com.simple.book.domain.user.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.simple.book.domain.user.dto.EmailVerificationDto;
import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.domain.user.entity.EmailVerification;
import com.simple.book.domain.user.repository.EmailVerificationRepository;
import com.simple.book.global.advice.ResponseMessage;
import com.simple.book.global.config.ApplicationConfig;
import com.simple.book.global.exception.UnknownException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailVerificationService {
	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private EmailVerificationRepository emailVerificationRepository;

	@Autowired
	private TemplateEngine templateEngine;

	/**
	 * 이메일 인증 Request
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
//	public ResponseMessage sendVerificationEmail(EmailVerificationRequestDto dto) throws RuntimeException {
//		SimpleMailMessage message = new SimpleMailMessage();
//		EmailVerificationRequestDto setTokenDto = createVerificationToken(dto);
//		
//		try {
//			message.setTo(setTokenDto.getEmail());
//			message.setFrom("sync@sync-team.co.kr");
//			message.setSubject("[sync] 회원가입 인증 입니다.");
//			message.setText("Your verification token is: " + setTokenDto.getToken());
//			javaMailSender.send(message);
//		} catch (Exception e) {
//			log.error(e.getStackTrace());
//			throw new RuntimeException(e);
//		}
//
//		return ResponseMessage.builder().message("전송 완료").build();
//	}

	/**
	 * HTML 기반 이메일 인증
	 * 
	 * @param dto
	 * @return
	 * @throws MessagingException
	 * @throws RuntimeException
	 */
	public ResponseMessage sendVerificationEmail(EmailVerificationRequestDto body) throws MessagingException {
		String token = createVerificationToken(body);
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		Context context = new Context();
		context.setVariable("token", token);

		String htmlContent = templateEngine.process("email/delete_account.html", context);

		try {
			helper.setTo(body.getEmail());
			helper.setFrom("sync@sync-team.co.kr");
			helper.setSubject("[sync] 회원탈퇴 인증 입니다.");
			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new UnknownException(e.getMessage());
		}

		return ResponseMessage.builder().message("전송 완료").build();
	}

	public String createVerificationToken(EmailVerificationRequestDto body) {
		String token;
		try {
			token = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
			EmailVerificationDto dto = EmailVerificationDto.builder().token(token).email(body.getEmail()).build();
			emailVerificationRepository.saveAndFlush(dto.toEntity());
		} catch (Exception e) {
			throw new UnknownException(e.getMessage());
		}
		return token;
	}

	/**
	 * 이메일 인증 Response
	 * 
	 * @param token
	 * @param email
	 * @return
	 */
	public ResponseMessage verificationEmail(String token, String email) {
		Optional<EmailVerification> response = emailVerificationRepository.findByEmail(email);
		if (!response.isEmpty()) {
			EmailVerification entity = response.get();
			boolean requestExpires = isRequestExpires(entity.getUpdDate());
			if (entity.toDto().getToken().equals(token) && !requestExpires) {
				return ResponseMessage.builder().message("인증 완료").build();
			} else if (requestExpires) {
				return ResponseMessage.builder().message("요청이 만료되었습니다.").result(false).build();
			} else {
				return ResponseMessage.builder().message("인증 번호를 확인 해 주세요.").result(false).build();
			}
		} else {
			// 보낸 요청이 없음
			return ResponseMessage.builder().message("유효한 요청이 없습니다.").result(false).build();
		}
	}

	private boolean isRequestExpires(Timestamp updDate) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		long thirtyMinutesInMillis = 30 * 60 * 1000;
		return currentTime.getTime() - updDate.getTime() >= thirtyMinutesInMillis;
	}

	/**
	 * 만료된 인증 정보 삭제
	 * 
	 * @throws Exception
	 */
	@Transactional
	public void deleteEmailToken() throws RuntimeException {
		List<EmailVerification> reqList = emailVerificationRepository.findAll();
		if (reqList.size() > 0) {
			for (EmailVerification entity : reqList) {
				String email = entity.toDto().getEmail();
				Timestamp updDate = entity.getUpdDate();
				if (isRequestExpires(updDate)) {
					emailVerificationRepository.deleteByEmail(email);
				}
			}
		}
	}
}
