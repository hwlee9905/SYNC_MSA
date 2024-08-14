package user.service.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;
@Setter
@Getter
@Schema(description = "업무를 수정하기 위한 DTO")
public class UpdateTaskRequestDto {
    @Schema(description = "업무 내용")
    private Optional<String> description = Optional.empty();
    @Schema(description = "업무 종료일")
    private Optional<Date> endDate = Optional.empty();
    @Schema(description = "업무 시작일")
    private Optional<Date> startDate = Optional.empty();
    @NotNull(message = "상태는 필수 입력 값 입니다.")
    @Pattern(regexp = "0|1|2", message = "상태는 0, 1, 2 중 하나여야 합니다.")
    @Schema(description = "업무 상태 ( 0: 진행중, 1: 완료, 2: 보류)")
    private int status;
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    @Schema(description = "업무 이름")
    private Optional<String> title = Optional.empty();
    @Schema(description = "수정할 업무의 프로젝트 아이디")
    @NotNull(message = "프로젝트 아이디는 필수 입력 값 입니다.")
    private Long projectId;
    @Schema(description = "수정할 업무의 아이디")
    @NotNull(message = "업무 아이디는 필수 입력 값 입니다.")
    private Long taskId;
}
