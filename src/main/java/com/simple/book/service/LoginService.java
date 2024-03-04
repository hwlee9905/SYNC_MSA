package com.simple.book.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	public HashMap<String, Object> login(HashMap<String, Object> body, HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();
		String status = "false";
		
		String id = String.valueOf(body.get("id"));
		String password = String.valueOf(body.get("password"));
		Optional<UserEntity> userEntity = userRepository.findByIdAndPassword(id, password);
		if (userEntity.isPresent()) {
			UserEntity entity = userEntity.get();
			setSession(session, entity);
			status = id;
		}
		result.put("result", status);
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
