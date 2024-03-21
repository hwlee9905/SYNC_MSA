package com.simple.book.domain.user.contoller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UserService userService;

	@PostMapping("/search")
	public ResponseEntity<String> search(HttpSession session, @RequestBody HashMap<String, Object> body)
			throws Exception {
		HashMap<String, Object> result = userService.findUser(session, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> body) throws Exception{
		HashMap<String, Object> result = userService.signup(body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
	@GetMapping("/delete")
	public ResponseEntity<String> deleteId(HttpSession session) throws Exception {
		HashMap<String, Object> result = userService.deleteUser(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
	@PostMapping("/modify")
	public ResponseEntity<String> modify(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception{
		HashMap<String, Object> result = userService.modifyUser(body, session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
