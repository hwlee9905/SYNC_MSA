package project.service.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseMessage {
	private String message;
	private boolean result;
	private Object value;
}
