package com.simple.book.domain.member.entity;

import com.simple.book.domain.task.entity.Task;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskMember {
    @EmbeddedId
    private TaskMemberId id;
    @ManyToOne
    @MapsId("mappingTaskId")
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne
    @MapsId("mappingMemberId")
    @JoinColumn(name = "member_id")
    private Member member;
}

