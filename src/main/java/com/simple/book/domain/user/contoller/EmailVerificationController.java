package com.simple.book.domain.user.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.user.dto.request.EmailVerificationRequestDto;
import com.simple.book.domain.user.service.EmailVerificationService;
import com.simple.book.global.util.ResponseMessage;

@RestController
@RequestMapping("/verify")
public class EmailVerificationController {

	@Autowired
	private EmailVerificationService emailVerificationService;
	
	@PostMapping("/email/send")
    public ResponseEntity<ResponseMessage> sendVerifyEmail(@RequestBody EmailVerificationRequestDto dto) throws Exception {
		ResponseMessage message = emailVerificationService.sendVerificationEmail(dto);
		return ResponseEntity.ok(message);
    }
	
	@GetMapping("/email")
	public ResponseEntity<ResponseMessage> verifyEmail(@RequestParam(name="token") String token, @RequestParam(name="email") String email){
		ResponseMessage message = emailVerificationService.verificationEmail(token, email);
		return ResponseEntity.ok(message);
	}
}
