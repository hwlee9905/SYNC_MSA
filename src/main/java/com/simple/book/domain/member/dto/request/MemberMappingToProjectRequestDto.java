package com.simple.book.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberMappingToProjectRequestDto {
    private String userId;
    private Long projectId;
    private Boolean isManager;
}
