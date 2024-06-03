package com.simple.book.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberMappingRequestDto {
    private String userId;
    private Long projectId;
    private Boolean isManager;
}
