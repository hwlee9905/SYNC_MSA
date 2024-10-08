package user.service.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import user.service.global.advice.LogAop;

@Controller
public class Test2Controller {
	@GetMapping("/")
	public String index(HttpServletResponse response) {
		return "index.html";
	}
	@GetMapping("/test")
	@LogAop
	@ResponseBody
	public String test() {
		return "hello";
	}
}
