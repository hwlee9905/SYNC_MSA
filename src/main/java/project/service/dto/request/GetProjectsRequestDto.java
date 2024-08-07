package project.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProjectsRequestDto {
    private List<Long> projectIds;
}