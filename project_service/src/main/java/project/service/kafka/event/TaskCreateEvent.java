package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.service.dto.request.CreateTaskRequestDto;

import java.util.List;

@Getter
@Setter
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
