package com.simple.book.domain.log.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class LogService {
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;
	
	public LogService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate=kafkaTemplate;
	}
//	public void sendMessage(String topic, String message) {
//        kafkaTemplate.send(topic, message);
//    }
	
	@PostConstruct
	private void readLog() {
		Path logDir = Paths.get("/takebook/logs");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    Path fileName = (Path) event.context();
                    Path fullPath = logDir.resolve(fileName);

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("파일 수정됨: " + fullPath);

                        try {
                            List<String> lines = Files.readAllLines(fullPath);
                            for (String line : lines) {
                                System.out.println(line);
                                kafkaTemplate.send("log", line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@PostConstruct
	@KafkaListener(topics = "log", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
    	messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
