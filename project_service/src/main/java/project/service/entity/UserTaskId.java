package project.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTaskId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    private Long taskId;
}