package com.simple.book.domain.user.contoller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.user.dto.JwtToken;
import com.simple.book.domain.user.service.TokenManagementService;
import com.simple.book.domain.user.service.TokenService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TokenManagementController {
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private TokenManagementService tokenManagementService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@PostMapping("/login")
	public ResponseEntity<JwtToken> login(@RequestParam("userId") String userId, @RequestParam("password") String password) {
		JwtToken token = tokenService.login(userId, password);
	    return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) throws Exception {
		HashMap<String, Object> result = tokenManagementService.logout(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
