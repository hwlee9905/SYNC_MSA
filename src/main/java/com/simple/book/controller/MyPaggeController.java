package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.DeleteUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/myPage")
public class MyPaggeController {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private DeleteUserService deleteUserService;

	@GetMapping("/delete")
	public ResponseEntity<String> deleteId(HttpSession session) throws Exception {
		HashMap<String, Object> result = deleteUserService.deleteId(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
