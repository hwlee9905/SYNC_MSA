package com.simple.book.controller;

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
import com.simple.book.service.user.DeleteUserService;
import com.simple.book.service.user.FindUserService;
import com.simple.book.service.user.ModifyUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private FindUserService findUserService;

	@Autowired
	private DeleteUserService deleteUserService;
	
	@Autowired
	private ModifyUserService modifyUserService;
	
	@PostMapping("/search")
	public ResponseEntity<String> search(HttpSession session, @RequestBody HashMap<String, Object> body)
			throws Exception {
		HashMap<String, Object> result = findUserService.search(session, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
	@GetMapping("/delete")
	public ResponseEntity<String> deleteId(HttpSession session) throws Exception {
		HashMap<String, Object> result = deleteUserService.deleteId(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
	
	@PostMapping("/modify")
	public ResponseEntity<String> modify(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception{
		HashMap<String, Object> result = modifyUserService.modify(body, session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
