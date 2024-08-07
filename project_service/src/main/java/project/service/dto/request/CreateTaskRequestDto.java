package project.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Setter
@Getter
public class CreateTaskRequestDto {
    private String description;
    private Date endDate;
    private Date startDate;
    private int status;
    private String title;
    private Optional<Long> parentTaskId;
    private Long projectId;

}
