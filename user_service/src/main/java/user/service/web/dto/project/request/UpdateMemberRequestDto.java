package user.service.web.dto.project.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRequestDto {
    @Schema(description = "유저 로그인 아이디")
    @NotNull(message = "유저 로그인 아이디는 필수값입니다.")
    private String userId;
    @Schema(description = "프로젝트 아이디")
    @NotNull(message = "프로젝트 아이디는 필수값입니다.")
    private Long projectId;
    @Schema(description = "멤버 권한")
    @NotNull(message = "멤버 권한은 필수값입니다.")
    @Min(value = 0, message = "멤버 권한은 0, 1, 2 중 하나여야 합니다.")
    @Max(value = 2, message = "멤버 권한은 0, 1, 2 중 하나여야 합니다.")
    private int isManager;
}
