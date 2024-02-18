package com.simple.book.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class MyPageService {
	@Autowired
	private UserRepository userRepository;
	
	public HashMap<String, Object> deleteId(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		
		String id = (String) session.getAttribute("id");
		if (id != null) {
			userRepository.deleteById(id);
			session.invalidate();
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return result;
	}
}
