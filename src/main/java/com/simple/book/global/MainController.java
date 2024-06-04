package com.simple.book.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/page/signup")
	public String pageSignup() {
		return "signup";
	}
	
	@GetMapping("/page/login")
	public String pageLogin() {
		return "login.html";
	}

	@GetMapping("/board")
	public String pageBoard() {
		return "board";
	}
	
	@GetMapping("/board/write")
	public String pageBoardWrite() {
		return "board_write";
	}
	
	@GetMapping("/chat")
	public String pageChat() {
		return "chat";
	}
	
	@GetMapping("/alarm")
	public String pageAlarm() {
		return "alarm";
	}
	
	
}
