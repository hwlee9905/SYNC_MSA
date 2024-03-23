package com.simple.book.domain.user.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {
    @GetMapping("")
    public String userHome(@RequestParam(required = false) String username, @RequestParam(required = false) String role, @RequestParam(required = false) String name, Model model){
        if(username != null && role != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", role);
            model.addAttribute("name", name);
        }
        return "index";
    }
}