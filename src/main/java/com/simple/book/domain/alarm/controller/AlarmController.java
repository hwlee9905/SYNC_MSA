package com.simple.book.domain.alarm.controller;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.service.AlarmService;
import com.simple.book.domain.jwt.dto.CustomUserDetails;
import com.simple.book.domain.oauth2.CustomOAuth2User;
import com.simple.book.domain.user.service.UserService;

@RestController
@RequestMapping("/api/user/alarm")
public class AlarmController {

	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private UserService userService;

//	@MessageMapping("/alarm")
//	@SendTo("/topic/messages")
//	public void socketSend(String message) {
//		alarmService.sendMessage("test", message);
//	}
	
	@GetMapping("/list")
	public ResponseEntity<?> getAlarmList() {
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok().body(alarmService.getAlarmList(userId));
	}
	
	@DeleteMapping("/close")
	public ResponseEntity<?> deleteAlarm(@RequestParam(name="alarmId") UUID alarmId){
		alarmService.deleteAlarm(alarmId);
		return ResponseEntity.ok().build();
	}
	
//	@PostMapping("/add/topic")
//	public void addTopic(@RequestBody AlarmDto body) {
//		alarmService.createTopic(body);
//	}
//
//	@DeleteMapping("/del/topic")
//	public void deleteTopic(@RequestBody AlarmDto body) {
//		alarmService.deleteTopic(body);
//	}

}
