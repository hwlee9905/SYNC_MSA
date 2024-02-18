package com.simple.book.util;

import org.springframework.stereotype.Component;

@Component
public class Regex {
	private final String idCheck = "^[a-zA-Z0-9]+$";
	
	private final String nameCheck = "^[a-zA-Z가-힣]+$";
	
	private final String password = "^[a-zA-Z가-힣0-9~`!@#$%^&*()_+|\\\\{}\\[\\],./<>?]+$";
	
	private final String birth = "^[0-9]+$";
	
	private final String gender = "^[A-Z]+$";
	
	public boolean getIdCheck(String str) {
		boolean matches = str.matches(idCheck);
		return matches;
	}
	
	public boolean getNameCheck(String str) {
		boolean matches = str.matches(nameCheck);
		return matches;
	}
	
	public boolean getPasswordCheck(String str) {
		boolean matches = str.matches(password);
		return matches;
	}
	
	public boolean getBirthCheck(String str) {
		boolean matches = str.matches(birth);
		return matches;
	}
	
	public boolean getGenderCheck(String str) {
		boolean matches = str.matches(gender);
		return matches;
	}

}
