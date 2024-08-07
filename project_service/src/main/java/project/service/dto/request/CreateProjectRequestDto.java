package project.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateProjectRequestDto {
    private String description;
    private String title;
    private String subTitle;
    private Date startDate;
    private Date endDate;
}
