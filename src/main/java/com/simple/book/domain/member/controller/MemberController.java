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
    public ResponseEntity<String> memberAddToProject(@RequestBody MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        memberService.memberAddToProject(memberMappingToProjectRequestDto);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/task/member/add")
    public ResponseEntity<String> memberAddToTask(@RequestBody MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        Member member = memberService.memberAddToTask(memberMappingToTaskRequestDto);
        return ResponseEntity.ok("OK");
    }
}
