package user.service.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import user.service.UserService;
import user.service.web.dto.request.SignupRequestDto;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
	private final UserService userService;

	@ResponseBody
	@PostMapping("signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
		userService.signup(signupRequestDto);
		return ResponseEntity.ok("OK");
	}

}