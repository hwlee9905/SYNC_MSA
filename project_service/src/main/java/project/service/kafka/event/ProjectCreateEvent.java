package project.service.kafka.event;

import lombok.Data;
import project.service.dto.request.CreateProjectRequestDto;

@Data
public class ProjectCreateEvent {
    private CreateProjectRequestDto projectCreateRequestDto;
    private byte[] img;
    private String userId;
}
