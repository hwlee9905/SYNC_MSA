package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		Boolean isFlag = loginService.login(String.valueOf(body.get("id")),
											String.valueOf(body.get("password")),
											session);
		result.put("result", isFlag);
//		return isFlag == true ?  "true" : "false";
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
