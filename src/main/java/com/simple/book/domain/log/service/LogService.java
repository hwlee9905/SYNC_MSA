package com.simple.book.domain.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;
	
	@KafkaListener(topics = "log", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
    	messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
