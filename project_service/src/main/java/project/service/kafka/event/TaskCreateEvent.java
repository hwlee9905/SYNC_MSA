package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.service.dto.request.CreateTaskRequestDto;

import java.util.List;

@Getter
@Setter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<MultipartFile> files;
}
