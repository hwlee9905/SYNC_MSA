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
import com.simple.book.service.follow.AddFollowService;
import com.simple.book.service.follow.CancelFollowService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/follow")
public class FollowController {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AddFollowService addFollowService;

	@Autowired
	private CancelFollowService cancelFollowService;

	@PostMapping("/add")
	public ResponseEntity<String> follow(HttpSession session, @RequestBody HashMap<String, Object> body)
			throws Exception {
		HashMap<String, Object> result = addFollowService.follow(session, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/cancel")
	public ResponseEntity<String> cancel(HttpSession session, @RequestBody HashMap<String, Object> body)
			throws Exception {
		HashMap<String, Object> result = cancelFollowService.cancelFollow(session, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
