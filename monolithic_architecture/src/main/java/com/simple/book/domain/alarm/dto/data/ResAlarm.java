package com.simple.book.domain.alarm.dto.data;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class ResAlarm {	
	private UUID alarmId;
	private String message;
	private Timestamp createdAt;
}
