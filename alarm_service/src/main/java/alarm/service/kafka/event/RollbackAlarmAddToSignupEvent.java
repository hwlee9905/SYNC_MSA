package alarm.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RollbackAlarmAddToSignupEvent {
	private Long userId;
}
