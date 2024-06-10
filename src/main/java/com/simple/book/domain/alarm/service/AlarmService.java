package com.simple.book.domain.alarm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.alarm.dto.AlarmDto;
import com.simple.book.domain.alarm.repository.AlarmRepository;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.UserRepository;

@Service
public class AlarmService {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	private final KafkaAdmin kafkaAdmin;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;

	@Autowired
	private AlarmRepository topicRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	public AlarmService(KafkaTemplate<String, String> kafkaTemplate, KafkaAdmin kafkaAdmin) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaAdmin = kafkaAdmin;
	}

	public void sendMessage(String topic, String message) {
		kafkaTemplate.send(topic, message);
	}
	
	/*
	 * 유저 알람
	 */
	@KafkaListener(topics = "User", containerFactory = "kafkaListenerContainerFactory")
	public void userAlarm(String message) {
		Map<String, Object> map;
		try {
			map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			throw new RuntimeException("시스템 오류");
		}
		long id = Long.valueOf(String.valueOf(map.get("id")));
		
		messagingTemplate.convertAndSend("/topic/user/" + id, map.get("message"));
	}
	
	/*
	 * 프로젝트 알람
	 */
	@KafkaListener(topics = "Project", containerFactory = "kafkaListenerContainerFactory")
	public void projectAlarm(String message) {
		messagingTemplate.convertAndSend("/topic/project", message);
	}
	
	/*
	 * 테스크 알람
	 */
	@KafkaListener(topics = "Task", containerFactory = "kafkaListenerContainerFactory")
	public void taskAlarm(String message) {
		messagingTemplate.convertAndSend("/topic/task", message);
	}
	
	public String getAlarmUrl(String userId) {
		Optional<Long> id = userRepository.findUserIdByAuthenticationUserId(userId);
		if (id.isPresent()) {
			return "/topic/user/" + id.get();
		} else {
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
		}
	}
	
	public void sendTaskManager(List<Long> memberId) {
		if (memberId.size() > 0) {
			for (long id : memberId) {
				Optional<User> user = userRepository.findById(id);
				String name = null;
				
				if(user.isPresent()) {
					name = user.get().getUsername();
				} else {
					throw new RuntimeException("시스템 오류가 발생하였습니다.");
				}
				
				String message = name + " 님이 담당자로 배정 되었습니다.";
				
				Map<String, Object> map = new HashMap<>();
				map.put("id", id);
				map.put("message", message);
				try {
					kafkaTemplate.send("User", objectMapper.writeValueAsString(map));
				} catch (Exception e) {
					throw new RuntimeException("시스템 오류가 발생하였습니다.", e);
				}
				saveAlarm(id, message);
			}
		}
	}
	
	private void saveAlarm(long id, String message) {
		AlarmDto dto = new AlarmDto();
		System.out.println("PK: " + dto.getAlarmId());
		dto.setUserId(id);
		dto.setMessage(message);
		try {
			topicRepository.save(dto.toEntity());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("시스템 오류가 발생하였습니다.", e);
		}
	}
	

	@KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
	public void listen(String message) {
		messagingTemplate.convertAndSend("/topic/messages", message);
	}

//	public void createTopic(AlarmDto body) {
//		NewTopic newTopic = new NewTopic(body.getName(), 1, (short) 1);
//		kafkaAdmin.createOrModifyTopics(newTopic);
//
//		topicRepository.save(setDto(body).toEntity());
//	}
//
//	public void deleteTopic(AlarmDto body) {
//		String topicname = body.getName();
//    	try (AdminClient adminClient = createAdminClient()) {
//            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicname));
//            KafkaFuture<Void> future = deleteTopicsResult.all();
//            future.get();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//
//		if (topicRepository.deleteByName(topicname) == 0) {
//			System.out.println("성공이다");
//		} else {
//			System.out.println("실패다");
//		}
//	}
//
//	private AlarmDto setDto(AlarmDto body) {
//		AlarmDto dto = new AlarmDto();
//		dto.setName(body.getName());
//		dto.setType(body.getType());
//		dto.setId(body.getId());
//		return dto;
//	}
//
//	private AdminClient createAdminClient() {
//		Map<String, Object> config = kafkaAdmin.getConfigurationProperties();
//		return AdminClient.create(config);
//	}

}