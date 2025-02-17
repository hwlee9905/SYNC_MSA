package user.service.kafka.task.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import user.service.web.dto.task.request.UpdateTaskRequestDto;

import java.util.List;

@AllArgsConstructor
@Getter
public class TaskUpdateEvent {
    private UpdateTaskRequestDto updateTaskRequestDto;
    private List<FileData> descriptionFiles;
    private List<FileData> deletedImages;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileData {
        private String fileName;
        private byte[] fileContent;
    }
}