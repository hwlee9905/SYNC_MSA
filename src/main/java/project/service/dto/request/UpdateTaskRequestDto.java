package project.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Setter
@Getter
public class UpdateTaskRequestDto {
    private String description;
    private Date endDate;
    private Date startDate;
    private int status;
    private String title;
    private Long projectId;
    private Long taskId;
}
