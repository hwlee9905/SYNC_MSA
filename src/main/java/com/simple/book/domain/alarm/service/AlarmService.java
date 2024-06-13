package com.simple.book.domain.alarm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import com.simple.book.domain.alarm.dto.data.ResAlarm;
import com.simple.book.domain.alarm.dto.data.SendAlarm;
import com.simple.book.domain.alarm.entity.Alarm;
import com.simple.book.domain.alarm.repository.AlarmRepository;
import com.simple.book.domain.alarm.repository.AlarmUrlRepository;
import com.simple.book.domain.member.repository.MemberRepository;
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
	private AlarmRepository alarmRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private AlarmUrlRepository alarmUrlRepository;
	
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
		String url = String.valueOf(map.get("url"));
		
		messagingTemplate.convertAndSend("/topic/user/" + url, message);
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
	
	public List<ResAlarm> getAlarmList(String userId) {
		List<ResAlarm> result = new ArrayList<>();
		List<Alarm> alarmList = alarmRepository.findByUserId(userId);
		if (alarmList.size() > 0) {
			for (Alarm entity : alarmList) {
				ResAlarm resAlarm = new ResAlarm();
				resAlarm.setAlarmId(entity.toDto().getAlarmId());
				resAlarm.setMessage(entity.toDto().getMessage());
				resAlarm.setCreatedAt(entity.getCreatedAt());
				result.add(resAlarm);
			}
		} 
		return result;
	}
	
	public void deleteAlarm(UUID alarmId) {
		try {
			alarmRepository.deleteById(alarmId);
		} catch(Exception e) {
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
		}
	}
	
	public void sendTaskManager(long memberId) {
		Optional<User> member = memberRepository.findUserIdByTaskMember(memberId);
		if (member.isPresent()) {
			long userId = member.get().getId();
			String name = member.get().getUsername();
			String message = name + " 님이 담당자로 배정 되었습니다.";
			
			UUID alarmId = saveAlarm(userId, message);
			Optional<UUID> url = alarmUrlRepository.findUrlByUser(userRepository.getReferenceById(userId));
			if (url.isPresent()) {
				sendAlarm(alarmId, url.get());
			} else {
				throw new RuntimeException("시스템 오류가 발생하였습니다.");
			}
		} else {
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
		}
	}
	
	private UUID saveAlarm(long userId, String message) {
		UUID result;
		AlarmDto dto = AlarmDto.builder()
				.user(userRepository.getReferenceById(userId))
				.message(message).build();
		try {
			result = alarmRepository.saveAndFlush(dto.toEntity()).toDto().getAlarmId();
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
		}
		return result;
	}
	
	private void sendAlarm (UUID alarmId, UUID url){
		Optional<Alarm> alarm = alarmRepository.findById(alarmId);
		if (alarm.isPresent()) {
			SendAlarm dto = SendAlarm.builder()
					.alarmId(alarm.get().toDto().getAlarmId())
					.url(url)
					.message(alarm.get().toDto().getMessage())
					.createdAt(alarm.get().getCreatedAt())
					.build();
			try {
				kafkaTemplate.send("User", objectMapper.writeValueAsString(dto));
			} catch (Exception e) {
				throw new RuntimeException("시스템 오류가 발생하였습니다.");
			}
		} else {
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
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