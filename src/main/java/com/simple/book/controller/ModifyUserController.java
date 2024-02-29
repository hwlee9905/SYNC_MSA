package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.ModifyUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class ModifyUserController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ModifyUserService modifyUserService;
	
	@PostMapping("/modify")
	public ResponseEntity<String> modify(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception{
		HashMap<String, Object> result = modifyUserService.modify(body, session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
