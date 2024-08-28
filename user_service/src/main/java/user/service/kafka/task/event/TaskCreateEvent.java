package user.service.kafka.task.event;
import org.springframework.web.multipart.MultipartFile;
import user.service.web.dto.task.request.CreateTaskRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<MultipartFile> files;

}
