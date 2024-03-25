package com.simple.book.domain.user.dto.request;

import com.simple.book.domain.user.entity.EmailVerification;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailVerificationRequestDto {
	private String token;
	private String email;
	
	@Builder
	public EmailVerificationRequestDto(String token, String email) {
		this.token=token;
		this.email=email;
	}
	
	public EmailVerification toEntity() {
		return EmailVerification.builder()
				.token(token)
				.email(email)
				.build();
	}
}
