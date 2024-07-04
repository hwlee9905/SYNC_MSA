package com.simple.book.domain.user.dto.response;

import lombok.Data;

@Data
public class GetUserInfoResponseDto {
	private String userId;
	private String profileImg;
	private String username;
	private String nickname;
	private String position;
	private String introduction;
}
