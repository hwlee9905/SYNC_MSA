package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.fried.AddFriendService;
import com.simple.book.service.fried.FriendAcceptService;
import com.simple.book.service.fried.FriendListService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	private AddFriendService addFriendService;

	@Autowired
	private FriendAcceptService friendAcceptService;
	
	@Autowired
	private FriendListService friendListService;

	@GetMapping("/list")
	@ResponseBody
	public HashMap<String, Object> getList(HttpSession session) {
		HashMap<String, Object> result = friendListService.getList(session);
		return result;
	}

	@GetMapping("/add")
	@ResponseBody
	public HashMap<String, Object> addFriend(HttpSession session, @RequestParam(value = "id") String id) {
		HashMap<String, Object> result = addFriendService.addFriend(session, id);
		return result;
	}

	@GetMapping("/accept")
	@ResponseBody
	public HashMap<String, Object> friendAccept(HttpSession session, @RequestParam(value = "id") String id) {
		HashMap<String, Object> result = friendAcceptService.friendAccept(session, id);
		return result;
	}
}
