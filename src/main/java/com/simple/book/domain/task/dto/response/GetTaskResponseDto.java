package com.simple.book.domain.task.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simple.book.domain.task.entity.Task;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean status;
    private Long memberId;
    private List<GetTaskResponseDto> subTasks;

    public static GetTaskResponseDto fromEntity(Task task) {
        List<GetTaskResponseDto> subTaskDtos = task.getSubTasks().stream()
                .map(GetTaskResponseDto::fromEntity)
                .collect(Collectors.toList());

        return GetTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .status(task.getStatus())
                .subTasks(subTaskDtos)
                .build();
    }
    public static GetTaskResponseDto fromEntityOnlyChildrenTasks(Task task) {
        return GetTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .status(task.getStatus())
                .memberId(task.getMember() != null ? task.getMember().getId() : null)
                .build();
    }
}