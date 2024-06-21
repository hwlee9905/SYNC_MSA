package com.simple.book.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.member.dto.request.MemberMappingToProjectRequestDto;
import com.simple.book.domain.member.dto.request.MemberMappingToTaskRequestDto;
import com.simple.book.domain.member.service.MemberService;
import com.simple.book.global.advice.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/project/member/add")
    public ResponseEntity<ResponseMessage> memberAddToProject(@RequestBody MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        return ResponseEntity.ok().body(memberService.memberAddToProject(memberMappingToProjectRequestDto));
    }

    @PostMapping("/task/member/add")
    public ResponseEntity<ResponseMessage> memberAddToTask(@RequestBody MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        return ResponseEntity.ok().body(memberService.memberAddToTask(memberMappingToTaskRequestDto));
    }
}
