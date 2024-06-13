package com.simple.book.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "프로젝트를 생성하기 위한 DTO")
public class ProjectCreateRequestDto {
    @Schema(description = "프로젝트 설명")
    private String description;
    @Schema(description = "프로젝트 이름")
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title;
}
