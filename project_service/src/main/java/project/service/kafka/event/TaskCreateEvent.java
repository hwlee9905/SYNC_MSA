package project.service.kafka.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.service.dto.request.CreateTaskRequestDto;

@Getter
@Setter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<FileData> files;
    private byte[] thumbnailByte;
    private String extsn;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class FileData implements project.service.global.FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
