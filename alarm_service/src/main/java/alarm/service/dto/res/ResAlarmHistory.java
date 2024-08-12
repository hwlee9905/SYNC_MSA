package alarm.service.dto.res;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResAlarmHistory {
	private UUID alarmId;
	private String message;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
