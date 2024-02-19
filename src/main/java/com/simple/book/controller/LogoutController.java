package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.LogoutService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	@Autowired
	private LogoutService logoutService;
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpSession session){
		HashMap<String, Object> result = logoutService.logout(session);
		return "success".equals(result.get("result")) == true ? "true" : "false";
	}
}
