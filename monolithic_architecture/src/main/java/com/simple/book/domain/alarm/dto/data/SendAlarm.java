package com.simple.book.domain.alarm.dto.data;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendAlarm {
	private UUID alarmId;
	private UUID url;
	private String message;
	private Timestamp createdAt;
}
