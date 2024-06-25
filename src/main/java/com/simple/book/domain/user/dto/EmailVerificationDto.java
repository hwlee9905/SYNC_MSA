package com.simple.book.domain.user.dto;

import com.simple.book.domain.user.entity.EmailVerification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailVerificationDto {
	private String email;
	
	@Schema(hidden = true)
	private String token;
	
	@Builder
	public EmailVerificationDto(String email, String token) {
		this.email=email;
		this.token=token;
	}
	
	public EmailVerification toEntity() {
		return EmailVerification.builder()
				.email(email)
				.token(token)
				.build();
	}
}
