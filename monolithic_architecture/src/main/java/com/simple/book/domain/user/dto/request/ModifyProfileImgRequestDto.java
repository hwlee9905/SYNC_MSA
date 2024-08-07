package com.simple.book.domain.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyProfileImgRequestDto {
	@Schema(description = "변경할 프로필 사진 파일")
	@NotBlank(message = "file은(는) 필수 입력 값입니다.")
	private MultipartFile profileImg;
}
