package user.service.web;

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
import user.service.global.advice.SuccessResponse;
import user.service.web.dto.invite.request.SendLinkRequestDto;

@RestController
@RequiredArgsConstructor
public class InviteController {
	private final InviteService inviteService;
	
	/**
	 * 초대링크 가져오기
	 * @param projectId
	 * @return
	 */
	@Operation(summary = "프로젝트 초대 URL 가져오기", description = "프로젝트 고유 초대 URL을 가져옵니다.")
	@GetMapping("/user/api/link")
	public ResponseEntity<SuccessResponse> getLink(@Parameter(description = "Project PK 값") @RequestParam(name = "projectId") long projectId){
		return ResponseEntity.ok().body(inviteService.getLink(projectId));
	}
	
	/**
	 * 이메일로 초대링크 전송
	 * @param projectId
	 * @return
	 */
	@Operation(summary = "프로젝트 이메일로 초대하기", description = "이메일로 프로젝트 초대장을 전송합니다.")
	@PostMapping("/user/api/email")
	public ResponseEntity<SuccessResponse> sendEmailLink(@RequestBody SendLinkRequestDto body){
		return ResponseEntity.ok().body(inviteService.sendEmailLink(body));
	}
}
