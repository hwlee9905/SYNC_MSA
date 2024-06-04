package com.simple.book.domain.alarm.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.dto.req.ReqTopicDto;
import com.simple.book.domain.alarm.service.AlarmService;

@RestController
public class AlarmController {
	
	@Autowired
	private AlarmService alarmService;
	
//	@PostMapping("/kafka/send")
//	public ResponseEntity<?> send(@RequestBody String message) {
//		messageService.sendMessage("test", message);
//		return ResponseEntity.status(HttpStatus.OK).body("완료");
//	}
	
//	@GetMapping("/kafka/read")
//	public ResponseEntity<?> listen(){
//		String message = messageService.read();
//		return ResponseEntity.status(HttpStatus.OK).body(message);
//	}
	
//	@MessageMapping("/kakfa")
//	@SendTo("/topic/text")
//	public void send(@RequestBody String message) {
//		System.out.println("message: " + message);
//	}
	
	@MessageMapping("/alarm")
	@SendTo("/topic/messages")
	public void socketSend(String message) {
		alarmService.sendMessage("test", message);
	}
	
	@PostMapping("/add/topic")
	public void addTopic(@RequestBody ReqTopicDto body) {
		alarmService.createTopic(body);
	}
	
	@DeleteMapping("/del/topic")
	public void deleteTopic(@RequestBody ReqTopicDto body) {
		alarmService.deleteTopic(body);
	}
}
