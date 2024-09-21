package user.service.kafka.task.event;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import user.service.web.dto.task.request.CreateTaskRequestDto;

@AllArgsConstructor
@Getter
public class TaskCreateEvent {
    private CreateTaskRequestDto createTaskRequestDto;
    private List<FileData> files;
    private byte[] thumbnailByte;
    private String extsn;
    
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자 추가
    @Getter
    public static class FileData {
        private String fileName;
        private byte[] fileContent;
    }
}
