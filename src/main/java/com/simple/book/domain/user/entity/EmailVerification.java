package com.simple.book.domain.user.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.simple.book.domain.user.dto.EmailVerificationDto;
import com.simple.book.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "email_verification")
@DynamicUpdate
@NoArgsConstructor
@Builder
public class EmailVerification extends BaseTimeEntity {
	@Id
	@Column(name = "email")
	private String email;
	
    @Column(name = "token")
    private String token;
    
    public EmailVerification (String email, String token) {
    	this.email=email;
    	this.token=token;
    }

    public EmailVerificationDto toDto() {
    	return EmailVerificationDto.builder()
    			.email(this.email)
    			.token(this.token)
    			.build();
    }
}
