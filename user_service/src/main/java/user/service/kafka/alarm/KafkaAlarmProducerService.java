package user.service.kafka.alarm;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import user.service.web.dto.alarm.request.AlarmListRequestDto;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.alarm.event.AlarmDeleteEvent;
import user.service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class KafkaAlarmProducerService {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final KafkaAdmin kafkaAdmin;

	// repository
	private final UserRepository userRepository;

	// common
	private final ObjectMapper objectMapper;
	private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

	private static final String ALARM_DELETE_TOPIC = "alarm-delete-topic";
	
	// controller

	public void getAlarmList(String loginId) {
		AlarmListRequestDto dto = new AlarmListRequestDto();
		User user = userRepository.findByAuthenticationUserId(loginId);
		dto.setUserId(user.getId());

		String mapper = null;
		try {
			mapper = objectMapper.writeValueAsString(dto);
		} catch (Exception e) {
			// Object mapper로 변환 불가능한 경우 처리
		}
		kafkaTemplate.send("reqAlarmList", mapper);
	}
	
	public void sendDeleteAlarm(UUID alarmId) {
		AlarmDeleteEvent event = new AlarmDeleteEvent(alarmId);
		ProducerRecord<String, Object> record = new ProducerRecord<>(ALARM_DELETE_TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
	}

//	public Flux<String> getAlarmList() {
//		return Flux.create(sink -> {
//			try {
//				String message = queue.take();
//				sink.next(message);
//			} catch (InterruptedException e) {
//				sink.error(e);
//				Thread.currentThread().interrupt();
//			}
//		});
//	}
//
//	@KafkaListener(topics = "resAlarmList")
//	public void listen(ConsumerRecord<?, ?> record) {
//		String message = record.value().toString();
//		queue.offer(message);
//	}
}
