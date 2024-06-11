package com.simple.book.domain.alarm.dto;

import com.simple.book.domain.alarm.entity.Alarm;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmDto {
	private long alarmId;
	private long userId;
	private String message;
	
	@Builder
	public AlarmDto(long alarmId, long userId, String message) {
		this.alarmId=alarmId;
		this.userId=userId;
		this.message=message;
	}
	
	public Alarm toEntity() {
		return Alarm.builder()
				.alarmId(alarmId)
				.userId(userId)
				.message(message)
				.build();
	}
	
}
