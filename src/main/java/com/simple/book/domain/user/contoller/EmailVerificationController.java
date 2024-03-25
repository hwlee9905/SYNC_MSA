package com.simple.book.domain.user.contoller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.domain.user.service.EmailService;
import com.simple.book.domain.user.service.EmailVerificationTokenService;
import com.simple.book.global.util.ResponseMessage;

@RestController
@RequestMapping("/verify")
public class EmailVerificationController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmailVerificationTokenService emailVerificationTokenService;
	
	@PostMapping("/email")
    public ResponseEntity<ResponseMessage> verifyEmail(@RequestBody EmailVerificationRequestDto dto) throws Exception {
		ResponseMessage message = emailService.sendVerificationEmail(emailVerificationTokenService.createVerificationToken(dto));
        if (message.isResult()) {
        	CompletableFuture.runAsync(() -> {
        		emailVerificationTokenService.deleteVerificationToken(dto.getToken());
        	});
        }
		return ResponseEntity.ok(message);
    }
}
