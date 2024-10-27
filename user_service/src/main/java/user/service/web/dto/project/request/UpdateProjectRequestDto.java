package user.service.web.dto.project.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class UpdateProjectRequestDto {
    @NotNull(message = "프로젝트 아이디는 필수 입력 값 입니다.")
    private Long projectId;
    
    @NotBlank(message = "프로젝트 설명은 필수 입력 값 입니다.")
    private String description;
    
    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    private String title;
    
    @NotBlank(message = "부제목은 필수 입력 값 입니다.")
    private String subTitle;
    
    @Schema(description = "아이콘")
    private String icon;

    @Schema(description = "프로젝트 시작일")
    private Date startDate;

    @Schema(description = "프로젝트 종료일")
    private Date endDate;
}