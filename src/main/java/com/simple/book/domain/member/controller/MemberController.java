package com.simple.book.domain.member.controller;

import com.simple.book.domain.member.dto.MemberMappingRequestDto;
import com.simple.book.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/project/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @ResponseBody
    @PostMapping("/add")
    public String memberAddToProject(@RequestBody MemberMappingRequestDto memberMappingRequestDto) {
        return memberService.memberAddToProject(memberMappingRequestDto);
    }
}
