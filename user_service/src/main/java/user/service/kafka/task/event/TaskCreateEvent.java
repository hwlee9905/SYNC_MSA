package user.service.kafka.task.event;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import user.service.web.dto.task.request.CreateTaskRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<FileData> files;
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자 추가
    @Getter
    public static class FileData {
        private String fileName;
        private byte[] content;
    }
}
