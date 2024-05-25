package com.simple.book.domain.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Setter
@Getter
public class CreateTaskRequestDto {
    private String description;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private Date endDate;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private Date startDate;
    @NotBlank(message = "상태는 필수 입력 값 입니다.")
    private Boolean status;
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String title;
    private Optional<Long> parentTaskId;
    @NotBlank(message = "프로젝트는 필수 입력 값 입니다.")
    private Long projectId;

}
