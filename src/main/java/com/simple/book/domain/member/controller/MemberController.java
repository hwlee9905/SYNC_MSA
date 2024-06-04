package com.simple.book.domain.member.controller;

import com.simple.book.domain.member.dto.request.MemberMappingToProjectRequestDto;
import com.simple.book.domain.member.dto.request.MemberMappingToTaskRequestDto;
import com.simple.book.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/project/member/add")
    public String memberAddToProject(@RequestBody MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        return memberService.memberAddToProject(memberMappingToProjectRequestDto);
    }

    @PostMapping("/task/member/add")
    public String memberAddToTask(@RequestBody MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        return memberService.memberAddToTask(memberMappingToTaskRequestDto);
    }
}
