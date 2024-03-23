package com.simple.book.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("")
	public String pageIndex() {
		return "index";
	}
	
	@GetMapping("board")
	public String pageBoard() {
		return "board";
	}
	
	@GetMapping("/board/write")
	public String pageBoardWrite() {
		return "board_write";
	}
}
