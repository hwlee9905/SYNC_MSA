package user.service.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import user.service.InviteService;
import user.service.MemberService;
import user.service.UserService;
import user.service.global.advice.SuccessResponse;
import user.service.web.dto.invite.request.ProjectInviteRequestDto;
import user.service.web.dto.invite.request.SendLinkRequestDto;
import user.service.web.dto.member.request.MemberMappingToProjectRequestDto;

@RestController
@RequiredArgsConstructor
public class InviteController {
	private final InviteService inviteService;
	private final UserService userService;
	private final MemberService memberService;
	
	/**
	 * 초대링크 가져오기
	 * @param projectId
	 * @return
	 */
	@Operation(summary = "프로젝트 초대 URL 가져오기", description = "프로젝트 고유 초대 URL을 가져옵니다.")
	@GetMapping("/user/api/link")
	public ResponseEntity<SuccessResponse> getLink(@Parameter(description = "Project PK 값") @RequestParam(name = "projectId") long projectId){
		return ResponseEntity.ok(inviteService.getLink(projectId));
	}
	
	/**
	 * 이메일 초대 링크 전송
	 * @param body
	 * @return
	 */
	@Operation(summary = "프로젝트 이메일로 초대하기", description = "이메일로 프로젝트 초대장을 전송합니다.")
	@PostMapping("/user/api/email")
	public ResponseEntity<SuccessResponse> sendEmailLink(@RequestBody SendLinkRequestDto body){
		String userId = userService.getCurrentUserId();
		Long countMembers = memberService.countMember(body.getProjectId());
		return ResponseEntity.ok(inviteService.sendEmailLink(body, userId, countMembers));
	}
	
	/**
	 * 초대 수락
	 * @param body
	 * @return
	 */
	@Operation(summary = "프로젝트 초대수락", description = "프로젝트 초대를 수락 합니다.")
	@PostMapping("/user/api/invite")
	public ResponseEntity<SuccessResponse> acceInvite(@RequestBody ProjectInviteRequestDto body){
		String userId = userService.getCurrentUserId();
		Long projectId = inviteService.getProjectId(body.getToken());
		MemberMappingToProjectRequestDto dto = setMemberMappingToProjectRequestDto(userId, projectId);
		return ResponseEntity.ok(memberService.memberAddToProject(dto));
	}
	
	private MemberMappingToProjectRequestDto setMemberMappingToProjectRequestDto(String userId, Long projectId) {
		List<String> userIds = new ArrayList<>();
		userIds.add(userId);
		
		MemberMappingToProjectRequestDto dto = MemberMappingToProjectRequestDto.builder()
				.userIds(userIds)
				.projectId(projectId)
				.isManager(0)
				.build();
		return dto;
	}
}
