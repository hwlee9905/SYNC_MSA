package alarm.service.kafka.event;

import lombok.Data;

@Data
public class AlarmUrlCreateEvent {
	private Long userId;
}
