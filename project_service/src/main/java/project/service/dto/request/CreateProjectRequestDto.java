package project.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateProjectRequestDto {
	// 프로젝트 설명
    private String description;
    // 프로젝트 이름
    private String title;
    // 프로젝트 부제목
    private String subTitle;
    // 프로젝트 썸네일
    private String thumbnail;
    // 프로젝트 썸네일 형식
    private char thumbnailType;
    // 프로젝트 시작 일
    private Date startDate;
    // 프로젝트 종료 일
    private Date endDate;
}
