package user.service.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import user.service.MemberService;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.task.KafkaTaskProducerService;
import user.service.web.dto.member.request.MemberMappingToProjectRequestDto;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.member.request.MemberRemoveRequestDto;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final KafkaTaskProducerService kafkaTaskProducerService;
    
    @Operation(summary = "프로젝트에 멤버를 추가하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : MemberMappingToProjectRequestDto")
    @PostMapping("user/api/member/project")
    public SuccessResponse memberAddToProject(@RequestBody @Valid MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        return memberService.memberAddToProject(memberMappingToProjectRequestDto);
    }
    
    @Operation(summary = "업무에 담당자를 추가하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : MemberMappingToTaskRequestDto")
    @PostMapping("user/api/member/task")
    public SuccessResponse memberAddToTask(@RequestBody @Valid MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        //없는 task id인 경우 보상트랜잭션 필요
        return kafkaTaskProducerService.sendAddUserToTaskEvent(memberMappingToTaskRequestDto);
    }

    //담당자 삭제 api
    @Operation(summary = "업무의 담당자들을 삭제하기 위한 API", description = "HOST = 150.136.153.235:30443")
    @DeleteMapping("node2/project/task/api/v1/users")
    public void getUsersFromTask(@RequestBody @Valid MemberRemoveRequestDto memberRemoveRequestDto) {
        //없는 task id인 경우 보상트랜잭션 필요
        kafkaTaskProducerService.sendRemoveUserFromTaskEvent(memberRemoveRequestDto);
    }
    @Operation(summary = "업무의 담당자들을 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("node2/project/task/api/v1/users")
    public void getUsersFromTask(@RequestParam Long taskId) {
    }
    
    @Operation(summary = "유저들의 멤버정보를 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("user/api/member/v1")
    public SuccessResponse getMembersByUserIds(@RequestParam List<Long> userIds) {
        return memberService.getMembersByUserIds(userIds);
    }
    
    @Operation(summary = "프로젝트의 멤버들을 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("user/api/member/v2")
    public SuccessResponse getUsersFromProject(@RequestParam List<Long> projectIds) {
        return memberService.getUsersFromProjects(projectIds);
    }
    @Operation(summary = "프로젝트에서 멤버를 삭제하기 위한 API", description = "HOST = 150.136.153.235:30443")
    @DeleteMapping("user/api/member/project")
    public SuccessResponse deleteMemberFromProject(@RequestBody @Valid MemberRemoveRequestDto memberRemoveRequestDto) {
        return memberService.deleteMemberFromProject(memberRemoveRequestDto);
    }
}
