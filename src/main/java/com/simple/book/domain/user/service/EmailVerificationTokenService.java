package com.simple.book.domain.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.domain.user.repository.EmailVerificationRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailVerificationTokenService {
	
	@Autowired
	private EmailVerificationRepository emailVerificationRepository;
	
	public EmailVerificationRequestDto createVerificationToken(EmailVerificationRequestDto dto) {
        String token = UUID.randomUUID().toString();
        dto.setToken(token);
        emailVerificationRepository.saveAndFlush(dto.toEntity());
        return dto;
    }
	
	public void deleteVerificationToken(String token) {
		try {
		    Thread.sleep(30 * 60 * 1000);
		} catch (InterruptedException e) {
		    log.error(e.getStackTrace());
		}
		emailVerificationRepository.deleteById(token);
	}
}
