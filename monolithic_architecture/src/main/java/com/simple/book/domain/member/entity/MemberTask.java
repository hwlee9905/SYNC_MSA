package com.simple.book.domain.member.entity;

import com.simple.book.domain.task.entity.Task;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_member",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "task_member_uk"
            , columnNames = {"task_id", "member_id"}
        )
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberTask {
    @EmbeddedId
    private MemberTaskId id;
    @ManyToOne
    @MapsId("mappingTaskId")
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne
    @MapsId("mappingMemberId")
    @JoinColumn(name = "member_id")
    private Member member;
}

