package com.simple.book.domain.alarm.controller;
import com.simple.book.domain.alarm.dto.req.ReqTopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.alarm.service.AlarmService;

@RestController
public class AlarmController {

	@Autowired
	private AlarmService alarmService;

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
