package project.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GetTaskRequestDto {
    @NotBlank(message = "업무 아이디는 필수 값입니다.")
    private Long taskId;
}
