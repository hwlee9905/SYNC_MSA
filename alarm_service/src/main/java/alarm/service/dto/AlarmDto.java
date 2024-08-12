package alarm.service.dto;

import java.util.UUID;

import alarm.service.entity.Alarm;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmDto {
	private UUID alarmId;
	private long userId;
	private String message;
	
	@Builder
	public AlarmDto(UUID alarmId, long userId, String message) {
		this.alarmId=alarmId;
		this.userId=userId;
		this.message=message;
	}
	
	public Alarm toEntity() {
		return Alarm.builder()
				.alarmId(alarmId)
				.userId(userId)
				.message(message)
				.build();
	}
}
