package com.simple.book.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.UserRepository;
import com.simple.book.util.DateFmt;
import com.simple.book.util.ValidationUserInfo;

@Service
public class SignupService {
	private final String SUCCESS = "success";

	private String id, password, email, firstName, lastName, birth, gender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	@Autowired
	private ValidationUserInfo validationUserInfo;

	public HashMap<String, Object> signup(HashMap<String, Object> body) {
		String key = "result";
		HashMap<String, Object> result = new HashMap<>();

		id = String.valueOf(body.get("id"));
		password = String.valueOf(body.get("password"));
		email = String.valueOf(body.get("email"));
		firstName = String.valueOf(body.get("first_name"));
		lastName = String.valueOf(body.get("last_name"));
		birth = (String.valueOf(body.get("birth")).replaceAll("-", ""));
		gender = String.valueOf(body.get("gender"));

		String resultId = validationUserInfo.getResultId(id);
		String resultPassword = validationUserInfo.getResultPassword(password);
		String resultEmail = validationUserInfo.getResultEmail(email);
		String resultFirstName = validationUserInfo.getResultFirstName(firstName);
		String resultLastName = validationUserInfo.getResultLastName(lastName);
		String resultBirth = validationUserInfo.getResultBirth(birth);
		String resultGender = validationUserInfo.getResultGender(gender);

		if (!resultId.equals(SUCCESS)) {
			result.put(key, resultId);
		} else if (!resultPassword.equals(SUCCESS)) {
			result.put(key, resultPassword);
		} else if (!resultEmail.equals(SUCCESS)) {
			result.put(key, resultEmail);
		} else if (!resultFirstName.equals(SUCCESS)) {
			result.put(key, resultFirstName);
		} else if (!resultLastName.equals(SUCCESS)) {
			result.put(key, resultLastName);
		} else if (!resultBirth.equals(SUCCESS)) {
			result.put(key, resultBirth);
		} else if (!resultGender.equals(SUCCESS)) {
			result.put(key, resultGender);
		} else {
			String date = dateFmt.getDate("yyyyMMdd");
			String time = dateFmt.getDate("HHmmss");
			UserEntity entity = setEntity(date, time);

			userRepository.saveAndFlush(entity);
			result.put("result", SUCCESS);
		}
		return result;
	}

	private UserEntity setEntity(String date, String time) {
		UserEntity entity = new UserEntity();

		entity.setId(id);
		entity.setPassword(password);
		entity.setEmail(email);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setBirth(birth);
		entity.setGender(gender);
		entity.setDelYn("N");
		entity.setInsDate(date);
		entity.setInsTime(time);
		entity.setInsId(id);
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId(id);

		return entity;
	}

}
