package alarm.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import alarm.service.kafka.event.RollbackAlarmAddToSignupEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	
	public static final String ROLLBACK_ALARM_URL_ADD_TO_SIGNUP_TOPIC = "rollback-alarm-url-add-to-signup-topic";
	
	public void sendRollbackAlarmUrlAddToSignupEvent(Long userId) {
		RollbackAlarmAddToSignupEvent event = new RollbackAlarmAddToSignupEvent(userId);
		ProducerRecord<String, Object> record = new ProducerRecord<>(ROLLBACK_ALARM_URL_ADD_TO_SIGNUP_TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
	}
}
