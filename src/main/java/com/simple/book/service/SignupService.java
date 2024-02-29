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
	
	private String id, firstName, lastName, password, birth, gender;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DateFmt dateFmt;

	@Autowired
	private ValidationUserInfo validationUserInfo;

	public HashMap<String, Object> signup(HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();

		id = (String) body.get("id");
		firstName = (String) body.get("first_name");
		lastName = (String) body.get("last_name");
		password = (String) body.get("password");
		birth = ((String) body.get("birth")).replaceAll("-", "");
		gender = (String) body.get("gender");

		String resultId = validationUserInfo.getResultId(id);
		String resultFirstName = validationUserInfo.getResultFirstName(firstName);
		String resultLastName = validationUserInfo.getResultLastName(lastName);
		String resultPassword = validationUserInfo.getResultPassword(password);
		String resultBirth = validationUserInfo.getResultBirth(birth);
		String resultGender = validationUserInfo.getResultGender(gender);

		if (!resultId.equals(SUCCESS)) {
			result.put("result", resultId);
		} else if (!resultFirstName.equals(SUCCESS)) {
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
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setPassword(password);
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
