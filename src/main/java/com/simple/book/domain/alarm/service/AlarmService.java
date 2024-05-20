package com.simple.book.domain.alarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    public AlarmService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
    
    @KafkaListener(topics = "test", groupId = "console-consumer-53939")
    public void listen(String message) {
    	messagingTemplate.convertAndSend("/topic/messages", message);
    }
    
}
