package com.simple.book.domain.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GetTaskRequestDto {
    @NotBlank
    private Long taskId;
}
