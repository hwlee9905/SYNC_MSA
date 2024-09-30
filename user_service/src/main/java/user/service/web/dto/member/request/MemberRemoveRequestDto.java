package user.service.web.dto.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberRemoveRequestDto {
    @NotNull
    private Long taskId;
    @NotBlank
    private String userId;
}