package com.simple.book.global.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.simple.book.domain.user.service.EmailVerificationService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class EmailSchedule {
	
	@Autowired
	private EmailVerificationService emailVerificationService;
	
	@Scheduled(cron = "0 0 0 * * *")
//	@Scheduled(fixedRate = 60000)
	public void emailScheduler() throws RuntimeException {
		log.info("[EMAIL] DELETE START");
		emailVerificationService.deleteEmailToken();
	}
}
