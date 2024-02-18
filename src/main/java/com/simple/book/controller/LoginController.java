package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/login")
	@ResponseBody
	public HashMap<String, Object> login(@RequestParam(value="id") String id, @RequestParam(value="password") String password, HttpSession session) {
		HashMap<String, Object> result = loginService.login(id, password, session);
		return result;
	}
}
