package com.simple.book.domain.alarm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.service.AlarmUrlService;
import com.simple.book.domain.user.service.UserService;

@RestController
@RequestMapping("/api/user/alarm/url")
public class AlarmUrlController {
	@Autowired
	private AlarmUrlService alarmUrlService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/get")
	public ResponseEntity<?> getAlarmUrl(){
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok().body(alarmUrlService.getAlarmUrl(userId));
	}

}
