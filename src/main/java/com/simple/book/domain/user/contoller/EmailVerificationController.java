package com.simple.book.domain.user.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailVerificationController {

	@Autowired
	private EmailVerificationService emailVerificationService;
	
	@Operation(summary = "인증 요청", description = "회원가입 시, 이메일 인증 요청을 보냅니다.")
	@PostMapping("/email/send")
    public ResponseEntity<ResponseMessage> sendVerifyEmail(@RequestBody EmailVerificationRequestDto dto) throws Exception {
		ResponseMessage message = emailVerificationService.sendVerificationEmail(dto);
		return ResponseEntity.ok(message);
    }
	
	@Operation(summary = "인증 확인", description = "회원가입 시, 이메일 인증 요청을 받아 유효성 검사를 합니다.")
	@GetMapping("/email")
	public ResponseEntity<ResponseMessage> verifyEmail(@RequestParam(name="token") String token, @RequestParam(name="email") String email){
		ResponseMessage message = emailVerificationService.verificationEmail(token, email);
		return ResponseEntity.ok(message);
	}
}
