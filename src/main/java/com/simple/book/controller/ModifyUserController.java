package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.ModifyUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class ModifyUserController {
	
	@Autowired
	private ModifyUserService modifyUserService;
	
	@PostMapping("/modify")
	@ResponseBody
	public HashMap<String, Object> modify(@RequestBody HashMap<String, Object> body, HttpSession session){
		HashMap<String, Object> result = modifyUserService.modify(body, session);
		return result;
	}
}
