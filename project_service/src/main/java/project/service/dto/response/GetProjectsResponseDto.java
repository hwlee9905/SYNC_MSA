package project.service.dto.response;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@Getter
public class GetProjectsResponseDto {
    private Long projectId;
    private String title;
    private String description;
    private String subTitle;
    private Date startDate;
    private Date endDate;
    private String thumbnail;
    private char thumbnailType;
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
