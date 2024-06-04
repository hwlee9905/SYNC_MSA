package com.simple.book.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberMappingToTaskRequestDto {
    private Long memberId;
    private Long taskId;
}
