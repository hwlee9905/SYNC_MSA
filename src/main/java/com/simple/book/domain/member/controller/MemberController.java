package com.simple.book.domain.member.controller;

import com.simple.book.domain.member.dto.request.MemberMappingToProjectRequestDto;
import com.simple.book.domain.member.dto.request.MemberMappingToTaskRequestDto;
import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/project/member/add")
    public ResponseEntity<Member> memberAddToProject(@RequestBody MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        Member member = memberService.memberAddToProject(memberMappingToProjectRequestDto);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/task/member/add")
    public ResponseEntity<Member> memberAddToTask(@RequestBody MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        Member member = memberService.memberAddToTask(memberMappingToTaskRequestDto);
        return ResponseEntity.ok(member);
    }
}
