package alarm.service.entity;

import java.util.UUID;

import alarm.service.dto.AlarmDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alarm extends BaseEntity{
	@Id 
	@Column(name = "alarm_id")
	private UUID alarmId;
	
	//@Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED", nullable = false)
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	@Column(name = "message", length = 255, nullable = false)
	private String message;

	public AlarmDto toDto() {
		return AlarmDto.builder()
				.alarmId(alarmId)
				.userId(userId)
				.message(message)
				.build();
	}
}
