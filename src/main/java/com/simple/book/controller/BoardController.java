package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.book.service.board.BoardListSerivce;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardListSerivce boardListSerivce;
	
	@GetMapping("/list")
	@ResponseBody
	public HashMap<String, Object> getList(){
		HashMap<String, Object> result = boardListSerivce.boardList();
		return result;
	}
}
