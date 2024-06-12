package com.simple.book.domain.alarm.dto.data;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ReqAlarm {
	private UUID alarmId;
	private long userId;
	private String message;
}
