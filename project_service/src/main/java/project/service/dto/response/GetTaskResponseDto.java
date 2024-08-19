package project.service.dto.response;

import lombok.*;
import project.service.entity.Task;

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
    private int status;
    private int depth;
    private List<GetTaskResponseDto> subTasks;
    public static GetTaskResponseDto fromEntityOnlyChildrenTasks(Task task) {
        return GetTaskResponseDto.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .startDate(task.getStartDate())
            .endDate(task.getEndDate())
            .status(task.getStatus())
            .depth(task.getDepth())
            .subTasks(task.getSubTasks().stream()
                .map(child -> GetTaskResponseDto.builder()
                    .id(child.getId())
                    .title(child.getTitle())
                    .description(child.getDescription())
                    .startDate(child.getStartDate())
                    .endDate(child.getEndDate())
                    .status(child.getStatus())
                    .depth(child.getDepth())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

}