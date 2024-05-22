package com.simple.book.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberMappingRequestDto {
    private Long projectId;
    private Boolean isManager;
}
