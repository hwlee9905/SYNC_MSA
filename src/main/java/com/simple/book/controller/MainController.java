package com.simple.book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping(value = "/")
	public String index(Model model) {
		return "index";
	}

	@GetMapping(value = "/page/login")
	public String pageLogin(Model model) {
		return "login";
	}
	
	@GetMapping(value = "/page/signup")
	public String pageSignup(Model model) {
		return "signup";
	}
	
	@GetMapping("/myPage")
	public String myPage(Model model) {
		return "my_page";
	}
	
}
