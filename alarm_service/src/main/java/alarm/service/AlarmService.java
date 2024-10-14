package alarm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import alarm.service.dto.res.ResAlarmHistory;
import alarm.service.entity.Alarm;
import alarm.service.entity.AlarmUrl;
import alarm.service.global.SuccessResponse;
import alarm.service.kafka.KafkaProducerService;
import alarm.service.repository.AlarmRepository;
import alarm.service.repository.AlarmUrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlarmService {
	// reposotiry
	private final AlarmRepository alarmRepository;
	private final AlarmUrlRepository alarmUrlRepository;

	// common
//	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	
	private final KafkaProducerService kafkaProducerService;
	

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
		return SuccessResponse.builder().data(Collections.singletonMap("history", result)).build();
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public void deleteAlarm(UUID alarmId) {
		try {
			alarmRepository.deleteById(alarmId);
		} catch(Exception e) {
			throw new RuntimeException("시스템 오류가 발생하였습니다.");
		}
	}
	
	public SuccessResponse getAlarmUrl(Long userId) {
		String result = null;
		if (userId != null) {
			Optional<AlarmUrl> entity = alarmUrlRepository.findByUserId(userId);
			if (entity.isPresent()) {
				result = "/topic/user/" + entity.get().getUrl();
			} else {
				throw new RuntimeException("시스템 오류가 발생하였습니다.");
			}
		} else {
			throw new RuntimeException("로그인 후 이용해 주시길 바랍니다.");
		}
		return SuccessResponse.builder().data(Collections.singletonMap("url", result)).build();
	}

//	public void sendResAlarm(String topic, SuccessResponse message) {
//		String mapper = null;
//		try {
//			mapper = objectMapper.writeValueAsString(message);
//		} catch (Exception e) {
//			// Object mapper로 변환 불가능한 경우 처리
//			e.printStackTrace();
//		}
//		System.out.println("★ topic ★ : " + topic);
//		System.out.println("★ 내용물 ★ : " + mapper);
//		kafkaTemplate.send(topic, mapper);
//	}

	@Transactional(rollbackFor = { Exception.class })
	public void createAlarmUrl(Long userId) {
		AlarmUrl entity = AlarmUrl.builder().url(UUID.randomUUID()).userId(userId).build();
		try {
			alarmUrlRepository.saveAndFlush(entity);
		} catch (Exception e) {
			kafkaProducerService.sendRollbackAlarmUrlAddToSignupEvent(userId);
			throw new RuntimeException("시스템 오류가 발생하였습니다.", e);
		}
	}

}
