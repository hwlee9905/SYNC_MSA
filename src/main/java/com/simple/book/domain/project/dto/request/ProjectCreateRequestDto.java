package com.simple.book.domain.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequestDto {
    private String description;
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title;
}
