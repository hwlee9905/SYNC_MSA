package com.simple.book.domain.friend.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.friend.service.FriendService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/friend")
public class FriendController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private FriendService friendService;

	@GetMapping("/list")
	public ResponseEntity<String> getList(HttpSession session) throws Exception {
		HashMap<String, Object> result = friendService.getFriendList(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<String> getSearch(HttpSession session, @RequestParam(value = "req_id") String reqId)
			throws Exception {
		HashMap<String, Object> result = friendService.findFriend(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/add")
	public ResponseEntity<String> addFriend(HttpSession session, @RequestParam(value = "req_id") String reqId)
			throws Exception {
		HashMap<String, Object> result = friendService.addFriend(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/request")
	public ResponseEntity<String> requestFriend(HttpSession session) throws Exception {
		HashMap<String, Object> result = friendService.requestFriend(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/response")
	public ResponseEntity<String> responseFriend(HttpSession session) throws Exception {
		HashMap<String, Object> result = friendService.responseFriend(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/accept")
	public ResponseEntity<String> friendAccept(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception{
		HashMap<String, Object> result = friendService.friendAccept(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/cancel")
	public ResponseEntity<String> friendCancel(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception{
		HashMap<String, Object> result = friendService.requestCancle(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/reject")
	public ResponseEntity<String> friendReject(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception {
		HashMap<String, Object> result = friendService.responseReject(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
