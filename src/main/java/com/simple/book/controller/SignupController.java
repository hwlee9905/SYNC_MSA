package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.SignupService;

@Controller
@RequestMapping("/user")
public class SignupController {
	
	@Autowired
	private SignupService signupService;
	
	@PostMapping("/signup")
	@ResponseBody
	public HashMap<String, Object> signup(@RequestBody HashMap<String, Object> body){
		HashMap<String, Object> result = signupService.signup(body);
		return result;
	}
	
}
