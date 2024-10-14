package alarm.service.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmUrlDto {
	private UUID url;
	private Long userId;
	
	@Builder
	public AlarmUrlDto (UUID url, Long userId) {
		this.url = url;
		this.userId = userId;
	}
}
