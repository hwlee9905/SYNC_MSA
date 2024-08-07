package com.simple.book.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyUserInfoRequestDto {
	@Schema(description = "변경할 정보 type (N = nickname, P = position, I = introduction)")
	@NotBlank(message = "type은(는) 필수 입력 값입니다.")
	private String type;
	@Schema(description = "변경할 정보 값")
	@NotBlank(message = "value은(는) 필수 입력 값입니다.")
	private String value;
}
