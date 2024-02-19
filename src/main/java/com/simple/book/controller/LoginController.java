package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	@ResponseBody
	public String login(@RequestBody HashMap<String, Object> body, HttpSession session) {
		
		
		Boolean isFlag = loginService.login(String.valueOf(body.get("id")),
											String.valueOf(body.get("password")),
											session);
		
		return isFlag == true ?  "true" : "false";
	}
}
