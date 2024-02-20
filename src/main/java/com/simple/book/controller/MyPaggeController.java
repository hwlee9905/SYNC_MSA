package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.DeleteUserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/myPage")
public class MyPaggeController {
	
	@Autowired
	private DeleteUserService deleteUserService;
	
	@GetMapping("/delete")
	@ResponseBody
	public HashMap<String, Object> deleteId(HttpSession session){
		HashMap<String, Object> result = deleteUserService.deleteId(session);
		return result;
	}
}
