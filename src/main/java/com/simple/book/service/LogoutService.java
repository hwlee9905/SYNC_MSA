package com.simple.book.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class LogoutService {
	private final String SUCCESS = "success";

	public HashMap<String, Object> logout(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		session.invalidate();
		result.put("result", SUCCESS);
		return result;
	}
}
