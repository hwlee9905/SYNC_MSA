package com.simple.book.domain.alarm.dto.data;

import lombok.Getter;

@Getter
public class ReqAlarm {
	private long alarmId;
	private long userId;
	private String message;
}
