package com.simple.book.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "이메일 인증 정보")
public class EmailVerificationRequestDto {
	@Schema(description = "인증 할 이메일", required = true)
	private String email;
}