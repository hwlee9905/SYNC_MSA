package project.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Builder
@AllArgsConstructor
public class MemberRemoveRequestDto {
    @NotNull
    private Long taskId;
    @NotBlank
    private String userId;
}