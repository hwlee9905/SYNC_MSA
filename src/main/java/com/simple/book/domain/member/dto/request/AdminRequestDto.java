package com.simple.book.domain.member.dto.request;

import lombok.Data;

@Data
public class AdminRequestDto {
	private long memberId;
	private boolean isManager;
}
