package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.SignupService;

@Controller
@RequestMapping("/user")
public class SignupController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private SignupService signupService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> body) throws Exception{
		HashMap<String, Object> result = signupService.signup(body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
}
