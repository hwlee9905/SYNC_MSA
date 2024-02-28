package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.board.AddBoardService;
import com.simple.book.service.board.BoardListSerivce;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardListSerivce boardListSerivce;

	@Autowired
	private AddBoardService addBoardService;

	@GetMapping("/list")
	@ResponseBody
	public HashMap<String, Object> getBoard() {
		HashMap<String, Object> result = boardListSerivce.boardList();
		System.out.println("리액트에서호출");
		result.put("result", "리액트하이");
		return result;
	}

	@PostMapping("/add")
	@ResponseBody
	public String addBoard(HttpSession session, @RequestBody HashMap<String, Object> body) throws Exception {
		HashMap<String, Object> result = addBoardService.addBoard(session, body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(result);
	}

	@PostMapping("/add/file")
	@ResponseBody
	public String addBoard(HttpSession session, @RequestParam(value = "images", required = false) MultipartFile file) throws Exception {
		HashMap<String, Object> result = addBoardService.addBoard(session, file);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("결과: " + mapper.writeValueAsString(result));
		return mapper.writeValueAsString(result);
	}
}
