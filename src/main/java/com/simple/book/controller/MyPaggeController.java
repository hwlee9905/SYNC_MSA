package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.MyPageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/myPage")
public class MyPaggeController {
	
	@Autowired
	private MyPageService myPageService;
	
	@GetMapping("/delete")
	@ResponseBody
	public HashMap<String, Object> deleteId(HttpSession session){
		HashMap<String, Object> result = myPageService.deleteId(session);
		return result;
	}
}
