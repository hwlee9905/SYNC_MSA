package com.simple.book.domain.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ModifyProfileImgRequestDto {
private MultipartFile profileImg;
}
