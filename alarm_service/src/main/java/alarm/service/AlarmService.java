package alarm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import alarm.service.dto.res.ResAlarmHistory;
import alarm.service.entity.Alarm;
import alarm.service.global.SuccessResponse;
import alarm.service.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlarmService {
	// kafka
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaAdmin kafkaAdmin;
	private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;

	// reposotiry
	private final AlarmRepository alarmRepository;

	// common
//	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	

	/*
	 * 유저 알람
	 */
//	@KafkaListener(topics = "User", containerFactory = "kafkaListenerContainerFactory")
//	public void userAlarm(String message) {
//		Map<String, Object> map;
//		try {
//			map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
//			});
//		} catch (Exception e) {
//			throw new RuntimeException("시스템 오류");
//		}
//		String url = String.valueOf(map.get("url"));
//
//		messagingTemplate.convertAndSend("/topic/user/" + url, message);
//	}
	
	public SuccessResponse getAlarmHistory(long userId) {
		List<ResAlarmHistory> result = new ArrayList<>();
		List<Alarm> alarmList = alarmRepository.findByUserId(userId);
		if (alarmList.size() > 0) {
			for (Alarm entity : alarmList) {
				ResAlarmHistory dto = ResAlarmHistory
						.builder()
						.alarmId(entity.getAlarmId())
						.message(entity.getMessage())
						.createdAt(entity.getCreatedAt())
						.updatedAt(entity.getUpdatedAt())
						.build();
				result.add(dto);
			}
		}
		return SuccessResponse.builder().data(result).build();
	}

	public void sendResAlarm(String topic, SuccessResponse message) {
		String mapper = null;
		try {
			mapper = objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			// Object mapper로 변환 불가능한 경우 처리
			e.printStackTrace();
		}
		System.out.println("★ topic ★ : " + topic);
		System.out.println("★ 내용물 ★ : " + mapper);
		kafkaTemplate.send(topic, mapper);
	}

}
