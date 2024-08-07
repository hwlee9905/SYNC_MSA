package project.service.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class GetMemberFromTaskResponseDto {
    private List<Long> userIds;

}
