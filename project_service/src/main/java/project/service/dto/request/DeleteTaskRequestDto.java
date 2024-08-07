package project.service.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteTaskRequestDto {
    private Long taskId;
    private Long projectId;
}
