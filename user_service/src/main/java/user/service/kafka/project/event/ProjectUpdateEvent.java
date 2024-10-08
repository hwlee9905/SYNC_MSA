package user.service.kafka.project.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import user.service.web.dto.project.request.UpdateProjectRequestDto;

@AllArgsConstructor
@Getter
public class ProjectUpdateEvent {
    private final UpdateProjectRequestDto projectUpdateRequestDto;
    private byte[] thumbnail;
    private String extsn;
}
