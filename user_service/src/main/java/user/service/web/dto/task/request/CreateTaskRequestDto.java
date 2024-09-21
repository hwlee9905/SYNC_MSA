package user.service.web.dto.task.request;

import java.util.Date;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "업무를 생성하기 위한 DTO")
public class CreateTaskRequestDto {
    @Schema(description = "업무 내용")
    private String description;
    
    @Schema(description = "업무 종료일")
    private Date endDate;
    
    @Schema(description = "업무 시작일")
    private Date startDate;
    
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    @Schema(description = "업무 이름")
    private String title;
    
    @Schema(description = "아이콘")
    private String thumbnailIcon;
    
    @Schema(description = "상위 업무 아이디, null == 프로젝트 최상위 업무")
    private Optional<Long> parentTaskId;
    
    @Schema(description = "생성할 업무의 프로젝트 아이디")
    @NotNull(message = "프로젝트 아이디는 필수 입력 값 입니다.")
    private Long projectId;
    @NotNull(message = "상태는 필수 입력 값 입니다.")
    @Min(value = 0, message = "상태는 0, 1, 2 중 하나여야 합니다.")
    @Max(value = 2, message = "상태는 0, 1, 2 중 하나여야 합니다.")
    @Schema(description = "업무 상태 ( 0: 진행중, 1: 완료, 2: 보류)")
    private int status;
}
