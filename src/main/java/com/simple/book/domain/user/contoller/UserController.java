package com.simple.book.domain.user.contoller;

import java.util.HashMap;

import com.simple.book.domain.jwt.dto.CustomUserDetails;
import com.simple.book.domain.oauth2.CustomOAuth2User;
import com.simple.book.domain.user.dto.request.SignupRequestDto;
import com.simple.book.global.filter.LoginCheckInterceptor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	//.requestMatchers("/user").hasAnyAuthority("USER") USER 계정 로그인 필요



	@GetMapping("auth")
	public String userApi(){
		String userId = null;
		String name = null;
		String infoset = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {

			if (authentication instanceof OAuth2AuthenticationToken) {
				CustomOAuth2User oauthToken = (CustomOAuth2User) authentication.getPrincipal();
				userId = oauthToken.getUsername(); // OAuth2로 인증된 경우 사용자 ID 추출
				name = oauthToken.getName();
				infoset = oauthToken.getInfoSet().toString();

			} else if (authentication instanceof UsernamePasswordAuthenticationToken) {
				CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
				userId = customUserDetails.getUsername();
				name = customUserDetails.getName();
				infoset = customUserDetails.getInfoSet().toString();

			}
		}
		return "ID : "+ userId + " 이름 : " + name + "님 반갑습니다. 유저 컨트롤러입니다.";
	}

//	@PostMapping("/search")
//	public ResponseEntity<String> search(HttpSession session, @RequestBody HashMap<String, Object> body)
//			throws Exception {
//		HashMap<String, Object> result = userService.findUser(session, body);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@PostMapping("/signup")
//	public ResponseEntity<String> signup(@RequestBody HashMap<String, Object> body) throws Exception{
//		HashMap<String, Object> result = userService.signup(body);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@GetMapping("/delete")
//	public ResponseEntity<String> deleteId(HttpSession session) throws Exception {
//		HashMap<String, Object> result = userService.deleteUser(session);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
//
//	@PostMapping("/modify")
//	public ResponseEntity<String> modify(@RequestBody HashMap<String, Object> body, HttpSession session) throws Exception{
//		HashMap<String, Object> result = userService.modifyUser(body, session);
//		return new ResponseEntity<>(mapper.writeValueAsString(result), HttpStatus.OK);
//	}
}
