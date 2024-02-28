package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	@GetMapping("/test")
	public ResponseEntity<?> test() throws Exception{
		HashMap<String, Object> result = new HashMap<>();
		result.put("test", "TEST입니다.");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
