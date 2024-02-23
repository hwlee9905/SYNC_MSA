package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@ResponseBody
	public String getList(HttpSession session) throws Exception {
		HashMap<String, Object> result = friendListService.getFriendList(session);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
	}

	@GetMapping("/search")
	@ResponseBody
	public String getSearch(HttpSession session, @RequestParam(value = "req_id") String reqId) throws Exception {
		HashMap<String, Object> result = findFriendService.findFriend(session, reqId);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
	}

	@GetMapping("/add")
	@ResponseBody
	public HashMap<String, Object> addFriend(HttpSession session, @RequestParam(value = "req_id") String reqId) {
		HashMap<String, Object> result = addFriendService.addFriend(session, reqId);
		return result;
	}

	@GetMapping("/request")
	@ResponseBody
	public HashMap<String, Object> requestFriend(HttpSession session) {
		HashMap<String, Object> result = requestFriendService.requestFriend(session);
		return result;
	}

	@GetMapping("/response")
	@ResponseBody
	public HashMap<String, Object> responseFriend(HttpSession session) {
		HashMap<String, Object> result = responseFriendService.responseFriend(session);
		return result;
	}

	@GetMapping("/accept")
	@ResponseBody
	public HashMap<String, Object> friendAccept(HttpSession session, @RequestParam(value = "req_id") String reqId) {
		HashMap<String, Object> result = friendAcceptService.friendAccept(session, reqId);
		return result;
	}

	@PostMapping("/cancel")
	@ResponseBody
	public HashMap<String, Object> friendCancel(HttpSession session, @RequestParam(value = "req_id") String reqId) {
		HashMap<String, Object> result = friendCancelService.requestCancle(session, reqId);
		return result;
	}

	@GetMapping("/reject")
	@ResponseBody
	public HashMap<String, Object> friendReject(HttpSession session, @RequestParam(value = "req_id") String reqId) {
		HashMap<String, Object> result = friendRejectService.responseReject(session, reqId);
		return result;
	}
}
