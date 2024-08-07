package com.simple.book.domain.project.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
@Getter
@Schema(description = "해당 유저가 속한 프로젝트를 가져오는 RESPONSE DTO")
public class GetProjectsResponseDto {
    private Long projectId;
    private String title;
    private String subTitle;
    private String description;
    private Date startDate;
    private Date endDate;
    @Schema(description = "user entity의 id를 반환")
    private List<Long> memberIds = new ArrayList<>();
    public GetProjectsResponseDto(Long projectId, String title, String subTitle, String description, Date startDate, Date endDate, List<Long> memberIds) {
        this.projectId = projectId;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberIds = memberIds;
    }
}
