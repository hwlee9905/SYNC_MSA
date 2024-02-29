package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.board.AddBoardService;
import com.simple.book.service.board.BoardListSerivce;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private BoardListSerivce boardListSerivce;

	@Autowired
	private AddBoardService addBoardService;

	@GetMapping("/list")
	public ResponseEntity<String> getBoard() throws Exception{
		HashMap<String, Object> result = boardListSerivce.boardList();
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addBoard(HttpSession session, @RequestBody HashMap<String, Object> body) throws Exception {
		HashMap<String, Object> result = addBoardService.addBoard(session, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/add/file")
	public ResponseEntity<String> addBoard(HttpSession session, @RequestParam(value = "images", required = false) MultipartFile file) throws Exception {
		HashMap<String, Object> result = addBoardService.addBoard(session, file);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
