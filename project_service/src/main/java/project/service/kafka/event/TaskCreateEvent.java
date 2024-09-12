package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.global.FileData;

import java.util.List;

@Getter
@Setter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<FileData> files;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FileData implements project.service.global.FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
