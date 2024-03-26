package com.simple.book.domain.user.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.domain.user.entity.EmailVerification;
import com.simple.book.domain.user.repository.EmailVerificationRepository;
import com.simple.book.global.config.ApplicationConfig;
import com.simple.book.global.util.ResponseMessage;

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

	/**
	 * 이메일 인증 Request
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ResponseMessage sendVerificationEmail(EmailVerificationRequestDto dto) throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		EmailVerificationRequestDto setTokenDto = createVerificationToken(dto);
		try {
			message.setTo(setTokenDto.getEmail());
			message.setFrom(applicationConfig.getMailId() + "@naver.com");
			message.setSubject("Email Verification");
			message.setText("Your verification token is: " + setTokenDto.getToken());
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error(e.getStackTrace());
			throw new Exception(e);
		}

		return ResponseMessage.builder().message("전송 완료").build();
	}

	public EmailVerificationRequestDto createVerificationToken(EmailVerificationRequestDto dto) {
		String token = UUID.randomUUID().toString();
		dto.setToken(token);
		emailVerificationRepository.saveAndFlush(dto.toEntity());
		return dto;
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

	private Timestamp latestDate(List<EmailVerification> response) {
		Timestamp latestInsDate = response.get(0).getInsDate();
		for (EmailVerification entity : response) {
			if (entity.getInsDate().after(latestInsDate)) {
				latestInsDate = entity.getInsDate();
			}
		}
		return latestInsDate;
	}
}
