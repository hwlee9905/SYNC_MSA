package com.simple.book.domain.user.contoller;

import com.simple.book.domain.user.dto.request.SignupRequestDto;
import com.simple.book.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    @PostMapping("signup")
    public String signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        userService.signup(signupRequestDto);

        return "OK";
    }
    @GetMapping("")
    public String userHome(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "role", required = false) String role, @RequestParam(name = "name", required = false) String name, Model model){
        if(username != null && role != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", role);
            model.addAttribute("name", name);
        }
        return "index";
    }
}