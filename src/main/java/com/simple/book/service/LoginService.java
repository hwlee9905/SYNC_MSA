package com.simple.book.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	public HashMap<String, Object> login(String id, String password, HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();
		boolean isExists = false;
		boolean existsPassword = userRepository.existsByIdAndPassword(id, password);
		if (existsPassword) {
			UserEntity entity = userRepository.findAllById(id);
			setSession(session, entity);
			isExists = true;
		}
		result.put("result", isExists);
		return result;
	}
	
	private void setSession(HttpSession session, UserEntity entity) {
		session.setAttribute("id", entity.getId());
		session.setAttribute("firstName", entity.getFirstName());
		session.setAttribute("lastName", entity.getLastName());
		session.setAttribute("birth", entity.getBirth());
		session.setAttribute("gender", entity.getGender());
		session.setAttribute("insDate", entity.getInsDate());
		session.setAttribute("insTime", entity.getInsTime());
		session.setAttribute("insId", entity.getInsId());
		session.setAttribute("updDate", entity.getUpdDate());
		session.setAttribute("updTime", entity.getUpdTime());
		session.setAttribute("updId", entity.getUpdId());
	}
}
