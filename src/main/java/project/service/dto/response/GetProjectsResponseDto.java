package project.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
@AllArgsConstructor
@Getter
public class GetProjectsResponseDto {
    private Long projectId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Float progress;
}
