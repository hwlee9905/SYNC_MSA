package com.simple.book.domain.alarm.controller;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.service.AlarmService;
import com.simple.book.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(value = "/api/user/alarm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmController {

	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private UserService userService;

	@Operation(summary = "과거 알림 불러오기", description = "데이터베이스에 저장 된 알림 정보를 불러옵니다.")
	@GetMapping("/list")
	public ResponseEntity<?> getAlarmList() {
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok().body(alarmService.getAlarmList(userId));
	}
	
	@Operation(summary = "과거 알림 닫기", description = "데이터베이스에 저장 된 알림을 삭제 합니다.")
	@DeleteMapping("/close")
	public ResponseEntity<?> deleteAlarm(@Parameter(description = "알림 PK 값") @RequestParam(name="alarmId") UUID alarmId){
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
