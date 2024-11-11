package alarm.service.kafka.event;

import java.util.UUID;

import lombok.Data;

@Data
public class AlarmDeleteEvent {
	private UUID alarmId;
}
