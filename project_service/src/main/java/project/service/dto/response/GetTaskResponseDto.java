package project.service.dto.response;

import lombok.*;
import project.service.entity.Task;

import java.io.File;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int status;
    private int depth;
    private float progress;
    private List<File> imageFiles; // 이미지 파일 목록 추가

    public static GetTaskResponseDto fromEntity(Task task, List<File> imageFiles) {
        float progress = 0.0f;
        if (task.getChildCount() > 0) {
            progress = (float) task.getChildCompleteCount() / task.getChildCount();
        }
        return GetTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .status(task.getStatus())
                .depth(task.getDepth())
                .progress(progress)
                .imageFiles(imageFiles)
                .build();
    }
}