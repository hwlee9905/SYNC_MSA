package com.simple.book.domain.board.controller;

import java.security.Principal;
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
import com.simple.book.domain.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private BoardService boardService;

	@GetMapping("/list")
	public ResponseEntity<String> getBoard(Principal principal) throws Exception {
		HashMap<String, Object> result = boardService.boardList(principal);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<String> addBoard(Principal principal, @RequestBody HashMap<String, Object> body) throws Exception {
		HashMap<String, Object> result = boardService.addBoard(principal, body);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}

	@PostMapping("/add/file")
	public ResponseEntity<String> imageUpload(@RequestParam(value = "images", required = false) MultipartFile file) throws Exception {
		HashMap<String, Object> result = boardService.imageUpload(file);
		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
	}
}
