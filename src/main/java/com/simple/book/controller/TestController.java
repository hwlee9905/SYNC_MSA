package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<String> test() throws Exception{
		HashMap<String, Object> result = new HashMap<>();
		result.put("test", "TEST입니다.");
		ObjectMapper mapper = new ObjectMapper();
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
