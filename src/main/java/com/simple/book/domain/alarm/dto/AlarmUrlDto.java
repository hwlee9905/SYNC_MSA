package com.simple.book.domain.alarm.dto;

import java.util.UUID;

import com.simple.book.domain.alarm.entity.AlarmUrl;
import com.simple.book.domain.user.entity.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmUrlDto {
	private UUID url;
	private User user;
	
	@Builder 
	public AlarmUrlDto(UUID url, User user) {
		this.url=url;
		this.user=user;
	}
	
	
	public AlarmUrl toEntity() {
		return AlarmUrl.builder()
				.url(url)
				.user(user)
				.build();
	}
	

}
