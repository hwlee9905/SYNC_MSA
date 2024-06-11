package com.simple.book.domain.alarm.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.service.AlarmService;
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
	
	@GetMapping("/getUrl")
	public ResponseEntity<?> getAlarmUrl(){
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok().body(alarmService.getAlarmUrl(userId));
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
