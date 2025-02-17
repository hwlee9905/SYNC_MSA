package project.service.kafka.event;

import lombok.*;
import project.service.dto.request.UpdateTaskRequestDto;

import java.util.List;

@Setter
@Getter
public class TaskUpdateEvent {
    private UpdateTaskRequestDto updateTaskRequestDto;
    private List<FileData> descriptionFiles;
    private List<FileData> deletedImages;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileData implements project.service.global.FileData{
        private String fileName;
        private byte[] fileContent;
    }
}
