package project.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMemberFromTaskRequestDto {
    @NotBlank(message = "업무 아이디는 필수 입력 값 입니다.")
    private Long taskId;
}