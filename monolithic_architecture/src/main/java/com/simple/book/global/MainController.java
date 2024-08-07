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
	
	@GetMapping("/producer")
	public String pageProducer() {
		return "producer";
	}
	
	@GetMapping("/consumer")
	public String pageConsumer() {
		return "consumer";
	}
	
	@GetMapping("/log")
	public String pageLog() {
		return "log";
	}
	
	@GetMapping("/alarm")
	public String pageAlarm() {
		return "alarm";
	}
	
	
}
