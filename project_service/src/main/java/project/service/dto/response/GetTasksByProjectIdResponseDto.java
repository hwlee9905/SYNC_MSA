package project.service.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTasksByProjectIdResponseDto {
    private Long taskId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int status;
    private int depth;
    private Task task;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Task {
        private int totalCount;
        private int completedCount;
    }
}