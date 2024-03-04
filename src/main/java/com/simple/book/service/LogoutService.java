package com.simple.book.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class LogoutService {

	public HashMap<String, Object> logout(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		session.invalidate();
		result.put("result", true);
		return result;
	}
}
