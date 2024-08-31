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
public class GetTasksByProjectIdResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int status;
    private int depth;
    private float progress;
}