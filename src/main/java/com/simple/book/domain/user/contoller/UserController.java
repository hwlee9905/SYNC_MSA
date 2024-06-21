package com.simple.book.domain.user.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.jwt.dto.CustomUserDetails;
import com.simple.book.domain.oauth2.CustomOAuth2User;
import com.simple.book.domain.user.service.UserService;
import com.simple.book.global.advice.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	//.requestMatchers("/user").hasAnyAuthority("USER") USER 계정 로그인 필요
	@GetMapping("auth")
	public String userApi() {
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
		return "ID : " + userId + " 이름 : " + name + "님 반갑습니다. 유저 컨트롤러입니다.";
	}
	
	@GetMapping("/info")
	public ResponseEntity<ResponseMessage> getUserInfo(){
		return ResponseEntity.ok().body(userService.getUserInfo());
	}

	@ResponseBody
	@PostMapping("remove")
	public ResponseEntity<ResponseMessage> removeUser() {
		return ResponseEntity.ok().body(userService.remove(userService.getCurrentUserId()));
	}

}
