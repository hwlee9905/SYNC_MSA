package com.simple.book.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.UserRepository;
import com.simple.book.util.DateFmt;
import com.simple.book.util.ValidationUserInfo;

import jakarta.servlet.http.HttpSession;

@Service
public class ModifyUserService {
	private final String SUCCESS = "success";

	private String firstName, lastName, password, birth, gender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	@Autowired
	private ValidationUserInfo validationUserInfo;

	public HashMap<String, Object> modify(HashMap<String, Object> body, HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();

		firstName = (String) body.get("first_name");
		lastName = (String) body.get("last_name");
		password = (String) body.get("password");
		birth = (String) body.get("birth");
		gender = (String) body.get("gender");

		String resultFirstName = validationUserInfo.getResultFirstName(firstName);
		String resultLastName = validationUserInfo.getResultLastName(lastName);
		String resultPassword = validationUserInfo.getResultPassword(password);
		String resultBirth = validationUserInfo.getResultBirth(birth);
		String resultGender = validationUserInfo.getResultGender(gender);

		if (session.getAttribute("id") == null) {
			result.put("result", "no_session");
		} else {
			if (!resultFirstName.equals(SUCCESS)) {
				result.put("result", resultFirstName);
			} else if (!resultLastName.equals(SUCCESS)) {
				result.put("result", resultLastName);
			} else if (!resultPassword.equals(SUCCESS)) {
				result.put("result", resultPassword);
			} else if (!resultBirth.equals(SUCCESS)) {
				result.put("result", resultBirth);
			} else if (!resultGender.equals(SUCCESS)) {
				result.put("result", resultGender);
			} else {
				String date = dateFmt.getDate("YYYYMMdd");
				String time = dateFmt.getDate("HHmmss");
				UserEntity entity = setEntity(date, time, session);

				userRepository.saveAndFlush(entity);
				result.put("result", SUCCESS);
			}
		}

		return result;

	}

	private UserEntity setEntity(String date, String time, HttpSession session) {
		UserEntity entity = new UserEntity();

		entity.setId((String) session.getAttribute("id"));
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setPassword(password);
		entity.setBirth(birth);
		entity.setGender(gender);
		entity.setDelYn("N");
		entity.setInsDate((String) session.getAttribute("insDate"));
		entity.setInsTime((String) session.getAttribute("insTime"));
		entity.setInsId((String) session.getAttribute("insId"));
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId((String) session.getAttribute("id"));

		return entity;
	}
}
