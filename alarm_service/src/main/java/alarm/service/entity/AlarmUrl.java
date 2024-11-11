package alarm.service.entity;

import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import alarm.service.dto.AlarmUrlDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm_url")
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmUrl extends BaseEntity{
	@Id
	private UUID url;
	
//	@Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED", nullable = false)
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	public AlarmUrlDto toDto() {
		return AlarmUrlDto.builder()
				.url(url)
				.userId(userId)
				.build();
	}
}
