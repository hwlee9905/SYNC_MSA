package user.service.web.dto.project.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private int isManager;
}
