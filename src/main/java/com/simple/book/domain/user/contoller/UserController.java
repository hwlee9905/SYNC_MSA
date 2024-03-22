package com.simple.book.domain.user.contoller;

import java.util.HashMap;

import com.simple.book.domain.user.dto.request.SignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UserController {
	//.requestMatchers("/user").hasAnyAuthority("USER") USER 계정 로그인 필요
	private final UserService userService;
	@PostMapping("/signup")
	public String signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
		userService.signup(signupRequestDto);

		return "OK";
	}

	@GetMapping("/user")
	public String userApi(){
		return "유저로 로그인하셨습니다. 유저 컨트롤러입니다.";
	}

//	@PostMapping("/search")
//	public ResponseEntity<String> search(HttpSession session, @RequestBody HashMap<String, Object> body)
//			throws Exception {
//		HashMap<String, Object> result = userService.findUser(session, body);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@PostMapping("/signup")
//	public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> body) throws Exception{
//		HashMap<String, Object> result = userService.signup(body);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@GetMapping("/delete")
//	public ResponseEntity<String> deleteId(HttpSession session) throws Exception {
//		HashMap<String, Object> result = userService.deleteUser(session);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@PostMapping("/modify")
//	public ResponseEntity<String> modify(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception{
//		HashMap<String, Object> result = userService.modifyUser(body, session);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
}
