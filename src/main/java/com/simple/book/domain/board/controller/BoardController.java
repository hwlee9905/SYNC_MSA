package com.simple.book.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.board.dto.BoardDto;
import com.simple.book.domain.board.service.BoardService;
import com.simple.book.global.util.ResponseMessage;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@PostMapping(value="/add")
	public ResponseEntity<ResponseMessage> addBoard(@RequestBody BoardDto body) throws Exception {
		return ResponseEntity.ok(boardService.addBoard(body));
	}

//	@PostMapping("/add/file")
//	public ResponseEntity<String> imageUpload(@RequestParam(value = "images", required = false) MultipartFile file) throws Exception {
//		HashMap<String, Object> result = boardService.imageUpload(file);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}

}
