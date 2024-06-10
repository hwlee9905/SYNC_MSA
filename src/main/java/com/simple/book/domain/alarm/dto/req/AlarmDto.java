package com.simple.book.domain.alarm.dto.req;

import lombok.Data;

@Data
public class AlarmDto {
	private long alarmId;
	private long userId;
	private String message;
}
