package com.simple.book.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.global.config.ApplicationConfig;
import com.simple.book.global.util.ResponseMessage;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailService {
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	public ResponseMessage sendVerificationEmail(EmailVerificationRequestDto dto) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
	        message.setTo(dto.getEmail());
	        message.setFrom(applicationConfig.getMailId() + "@naver.com");
	        message.setSubject("Email Verification");
	        message.setText("Your verification token is: " + dto.getToken());
	        javaMailSender.send(message);
        } catch (Exception e) {
        	log.error(e.getStackTrace());
        	throw new Exception(e);
        }
        return ResponseMessage.builder().message("전송 완료").build();
    }
}
