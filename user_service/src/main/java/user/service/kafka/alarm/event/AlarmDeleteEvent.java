package user.service.kafka.alarm.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlarmDeleteEvent {
	private UUID alarmId;
}
