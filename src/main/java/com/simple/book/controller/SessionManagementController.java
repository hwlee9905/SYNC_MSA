package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.dto.JwtToken;
import com.simple.book.service.LogoutService;
import com.simple.book.service.TokenService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionManagementController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private LogoutService logoutService;

	@PostMapping("/login/proc")
	public JwtToken login(@RequestParam("userId") String userId, @RequestParam("password") String password) {
		System.out.println("userId : " + userId);
		System.out.println("password : " + password);
		JwtToken token = tokenService.login(userId, password);
	    return token;
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) throws Exception {
		HashMap<String, Object> result = logoutService.logout(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
