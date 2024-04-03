package com.simple.book.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simple.book.domain.board.dto.BoardDto;
import com.simple.book.domain.board.service.BoardService;
import com.simple.book.global.util.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(value = "/board", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Operation(summary = "글 저장", description = "작성한 글을 저장합니다.")
	@PostMapping(value = "/add", consumes = "multipart/form-data")
	public ResponseEntity<ResponseMessage> addBoard(@ModelAttribute BoardDto body, @Schema(description = "이미지 파일", required = false)@RequestPart(value="images", required = false) MultipartFile[] files) throws Exception {
		return ResponseEntity.ok(boardService.addBoard(body, files));
	}

	/**
	 * 파업한 CODE
	 */
//	@PostMapping(value = "/add/file", consumes = "multipart/form-data")
//	public ResponseEntity<ResponseMessage> imageUpload(@RequestParam(value = "images") MultipartFile file) throws Exception {
//		return ResponseEntity.ok(boardService.imageUpload(file));
//	}

}
