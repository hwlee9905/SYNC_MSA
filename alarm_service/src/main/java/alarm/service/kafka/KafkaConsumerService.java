package alarm.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import alarm.service.AlarmService;
import alarm.service.kafka.event.AlarmUrlCreateEvent;
import alarm.service.kafka.event.AlarmDeleteEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
	private final AlarmService alarmService;
	
	private static final String ALARM_URL_CREATE_TOPIC = "alarm-url-create-topic";
	private static final String ALARM_DELETE_TOPIC = "alarm-delete-topic";
	
	@KafkaListener(topics = ALARM_URL_CREATE_TOPIC, groupId = "alarm_url_create_group", containerFactory = "kafkaAlarmUrlCreateEventListenerContainerFactory")
	public void listenAlarmUrlCreateEvent(AlarmUrlCreateEvent event) {
		alarmService.createAlarmUrl(event.getUserId());
	}
	
	@KafkaListener(topics = ALARM_DELETE_TOPIC, groupId = "alarm_delete_group", containerFactory = "kafkaAlarmDeleteEventListenerContainerFactory")
	public void listenAlarmDeleteEvent(AlarmDeleteEvent event) {
		alarmService.deleteAlarm(event.getAlarmId());
	}

}
