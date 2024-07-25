package user.service.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.service.MemberService;
import user.service.global.advice.ResponseMessage;
import user.service.kafka.task.KafkaTaskProducerService;
import user.service.web.dto.member.request.MemberMappingToProjectRequestDto;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.task.request.GetMemberFromTaskRequestDto;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final KafkaTaskProducerService kafkaTaskProducerService;
    @PostMapping("user/api/member/project")
    public ResponseMessage memberAddToProject(@RequestBody @Valid MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        return memberService.memberAddToProject(memberMappingToProjectRequestDto);
    }
    @PostMapping("user/api/member/task")
    public ResponseMessage memberAddToTask(@RequestBody @Valid MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        return kafkaTaskProducerService.sendAddUserToTaskEvent(memberMappingToTaskRequestDto);
    }

}
