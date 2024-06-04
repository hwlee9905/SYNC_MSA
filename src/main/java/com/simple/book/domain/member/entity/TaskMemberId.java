package com.simple.book.domain.member.entity;

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
public class TaskMemberId implements Serializable {

    private Long mappingTaskId;

    private Long mappingMemberId;

}