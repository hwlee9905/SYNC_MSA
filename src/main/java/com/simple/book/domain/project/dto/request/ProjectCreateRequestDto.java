package com.simple.book.domain.project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequestDto {
    private String description;
    private String title;
}
