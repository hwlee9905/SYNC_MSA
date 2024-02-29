package com.simple.book.controller;

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
import com.simple.book.service.friend.AddFriendService;
import com.simple.book.service.friend.FindFriendService;
import com.simple.book.service.friend.FriendAcceptService;
import com.simple.book.service.friend.FriendCancelService;
import com.simple.book.service.friend.FriendListService;
import com.simple.book.service.friend.FriendRejectService;
import com.simple.book.service.friend.RequestFriendService;
import com.simple.book.service.friend.ResponseFriendService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/friend")
public class FriendController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private AddFriendService addFriendService;

	@Autowired
	private FindFriendService findFriendService;

	@Autowired
	private FriendAcceptService friendAcceptService;

	@Autowired
	private FriendListService friendListService;

	@Autowired
	private RequestFriendService requestFriendService;

	@Autowired
	private ResponseFriendService responseFriendService;

	@Autowired
	private FriendCancelService friendCancelService;

	@Autowired
	private FriendRejectService friendRejectService;
	
	@GetMapping("/list")
	public ResponseEntity<String> getList(HttpSession session) throws Exception {
		HashMap<String, Object> result = friendListService.getFriendList(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<String> getSearch(HttpSession session, @RequestParam(value = "req_id") String reqId)
			throws Exception {
		HashMap<String, Object> result = findFriendService.findFriend(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/add")
	public ResponseEntity<String> addFriend(HttpSession session, @RequestParam(value = "req_id") String reqId)
			throws Exception {
		HashMap<String, Object> result = addFriendService.addFriend(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/request")
	public ResponseEntity<String> requestFriend(HttpSession session) throws Exception {
		HashMap<String, Object> result = requestFriendService.requestFriend(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/response")
	public ResponseEntity<String> responseFriend(HttpSession session) throws Exception {
		HashMap<String, Object> result = responseFriendService.responseFriend(session);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/accept")
	public ResponseEntity<String> friendAccept(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception{
		HashMap<String, Object> result = friendAcceptService.friendAccept(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/cancel")
	public ResponseEntity<String> friendCancel(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception{
		HashMap<String, Object> result = friendCancelService.requestCancle(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@GetMapping("/reject")
	public ResponseEntity<String> friendReject(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception {
		HashMap<String, Object> result = friendRejectService.responseReject(session, reqId);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
