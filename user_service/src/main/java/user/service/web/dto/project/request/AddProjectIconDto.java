package user.service.web.dto.project.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "프로젝트 썸네일 지정")
public class AddProjectIconDto {
	@Schema(description = "프로젝트 썸네일 (아이콘)")
    private String thumbnail;
}
