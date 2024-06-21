package com.simple.book.domain.user.dto.request;

import lombok.Data;

@Data
public class ModifyUserInfoRequestDto {
	private String username;
	private String position;
	private String introduction;
}
