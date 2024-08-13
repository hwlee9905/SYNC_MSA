package user.service.web.dto.invite.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteMappingToProjectRequestDto {
	@Schema(description = "프로젝트 아이디, 해당 프로젝트가 존재하는지 확인하세요")
	@NotNull(message = "프로젝트 아이디는 필수값입니다.")
	private Long projectId;
}
