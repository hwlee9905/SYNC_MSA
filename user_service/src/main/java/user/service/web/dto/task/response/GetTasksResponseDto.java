package user.service.web.dto.task.response;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTasksResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int status;
    private int depth;
    private float progress;
    private List<GetTasksResponseDto> subTasks;

}