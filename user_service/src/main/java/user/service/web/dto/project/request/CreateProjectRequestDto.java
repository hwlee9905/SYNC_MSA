package user.service.web.dto.project.request;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "프로젝트를 생성하기 위한 DTO")
public class CreateProjectRequestDto {
    @Schema(description = "프로젝트 설명")
    @NotBlank(message = "설명은 필수 입력 값 입니다.")
    private String description;
    
    @Schema(description = "프로젝트 이름")
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title;
    
    @Schema(description = "프로젝트 부제목")
    @NotBlank(message = "부제목은 필수 입력 값 입니다.")
    private String subTitle;
    
    @Schema(description = "썸네일")
    private String thumbnail;
    
    @Schema(description = "썸네일 형식,  'I' : 이미지, 'C' : 아이콘, 'E' : 이모지, 'N' : 없음")
    private char thumbnailType;
    
    @Schema(description = "프로젝트 시작일")
    private Date startDate;
    
    @Schema(description = "프로젝트 종료일")
    private Date endDate;
}
