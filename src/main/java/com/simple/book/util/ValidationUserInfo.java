package com.simple.book.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.book.repository.UserRepository;

@Component
public class ValidationUserInfo {
	private final String SUCCESS = "success";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Regex regex;
	
	public String getResultId(String id) {
		return isIdCheck(id);
	}
	
	public String getResultFirstName(String firstName) {
		return isFirstNameCheck(firstName);
	}
	
	public String getResultLastName(String lastName) {
		return isLastNameCheck(lastName);
	}
	
	public String getResultPassword(String password) {
		return isPasswordCheck(password);
	}
	
	public String getResultBirth(String birth) {
		return isBirthCheck(birth);
	}
	
	public String getResultGender(String gender) {
		return isGenderCheck(gender);
	}
	
	private String isIdCheck(String id) {
		String message = "";
		boolean existsId = userRepository.existsById(id);
		if (id == null) {
			// 아이디를 입력해주세요.
			message = "no_id";
		} else if (id.length() < 8) {
			// 아이디 최소 자리.
			message = "min_over_id";
		} else if (id.length() > 16) {
			// 아이디 최대 자리.
			message = "max_over_id";
		} else if (!regex.getIdCheck(id)) {
			// 영대소문자, 숫자만 허용.
			message = "regex_false_id";
		} else if (existsId) {
			message = "exists_id";
		} else {
			message = SUCCESS;
		}
		return message;
	}

	private String isFirstNameCheck(String firstName) {
		String message = "";
		if (firstName == null) {
			message = "no_firstname";
		} else if (firstName.length() < 1) {
			message = "min_over_firstname";
		} else if (firstName.length() > 12) {
			message = "max_over_firstname";
		} else if (!regex.getNameCheck(firstName)) {
			message = "regex_false_firstname";
		} else {
			message = SUCCESS;
		}
		return message;
	}

	private String isLastNameCheck(String lastName) {
		String message = "";
		if (lastName == null) {
			message = "no_lastname";
		} else if (lastName.length() < 2) {
			message = "min_over_lastname";
		} else if (lastName.length() > 12) {
			message = "max_over_lastname";
		} else if (!regex.getNameCheck(lastName)) {
			message = "regex_false_lastname";
		} else {
			message = SUCCESS;
		}
		return message;
	}

	private String isPasswordCheck(String password) {
		String message = "";
		if (password == null) {
			message = "no_password";
		} else if (password.length() < 4) {
			message = "min_over_password";
		} else if (password.length() > 8) {
			message = "max_over_password";
		} else if (!regex.getPasswordCheck(password)) {
			message = "regex_false_password";
		} else {
			message = SUCCESS;
		}
		return message;
	}

	private String isBirthCheck(String birth) {
		String message = "";
		if (birth == null) {
			message = "no_birth";
		} else if (birth.length() < 8) {
			message = "min_over_birth";
		} else if (birth.length() > 8) {
			message = "max_over_birth";
		} else if (!regex.getBirthCheck(birth)) {
			message = "regex_false_birth";
		} else {
			message = SUCCESS;
		}

		return message;
	}

	private String isGenderCheck(String gender) {
		String message = "";
		if (gender == null) {
			message = "no_gender";
		} else if (gender.length() < 1) {
			message = "min_over_gender";
		} else if (gender.length() > 1) {
			message = "max_over_gender";
		} else if (!regex.getGenderCheck(gender)) {
			message = "regex_false_gender";
		} else {
			message = SUCCESS;
		}
		return message;
	}
}
