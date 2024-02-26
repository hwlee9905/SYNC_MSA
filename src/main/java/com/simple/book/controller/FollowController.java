package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.follow.AddFollowService;
import com.simple.book.service.follow.CancelFollowService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/follow")
public class FollowController {
	@Autowired
	private AddFollowService addFollowService;
	
	@Autowired
	private CancelFollowService cancelFollowService;

	@PostMapping("/add")
	@ResponseBody
	public String follow(HttpSession session, @RequestBody HashMap<String, Object> body) throws Exception {
		HashMap<String, Object> result = addFollowService.follow(session, body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
	}
	
	@PostMapping("/cancel")
	@ResponseBody
	public String cancel(HttpSession session, @RequestBody HashMap<String, Object> body) throws Exception {
		HashMap<String, Object> result = cancelFollowService.cancelFollow(session, body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
	}
}
