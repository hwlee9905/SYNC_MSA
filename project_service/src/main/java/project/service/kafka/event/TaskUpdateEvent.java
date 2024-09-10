package project.service.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.service.dto.request.UpdateTaskRequestDto;

import java.util.List;

@Setter
@Getter
public class TaskUpdateEvent {
    private UpdateTaskRequestDto updateTaskRequestDto;
    private List<FileData> descriptionFiles;
    private List<FileData> deletedImages;

    @Data
    @AllArgsConstructor
    public static class FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
