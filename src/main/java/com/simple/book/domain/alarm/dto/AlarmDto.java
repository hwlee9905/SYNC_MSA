package com.simple.book.domain.alarm.dto;

import com.simple.book.domain.alarm.entity.Alarm;
import com.simple.book.domain.user.entity.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmDto {
	private long alarmId;
	private User user;
	private String message;
	
	@Builder
	public AlarmDto(long alarmId, User user, String message) {
		this.alarmId=alarmId;
		this.user=user;
		this.message=message;
	}
	
	public Alarm toEntity() {
		return Alarm.builder()
				.alarmId(alarmId)
				.user(user)
				.message(message)
				.build();
	}
	
}
