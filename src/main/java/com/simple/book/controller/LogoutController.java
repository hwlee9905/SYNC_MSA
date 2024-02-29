package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.LogoutService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private LogoutService logoutService;
	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) throws Exception {
		HashMap<String, Object> result = logoutService.logout(session);
//		return "success".equals(result.get("result")) == true ? "true" : "false";
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
