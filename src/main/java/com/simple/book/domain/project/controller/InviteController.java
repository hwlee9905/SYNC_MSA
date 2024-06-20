package com.simple.book.domain.project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.project.service.InviteService;
import com.simple.book.global.advice.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user/invite", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InviteController {
	private final InviteService inviteService;

	/**
	 * 초대링크 가져오기
	 * @param projectId
	 * @return
	 */
	@Operation(summary = "프로젝트 초대 URL 가져오기", description = "프로젝트 고유 초대 URL을 가져옵니다.")
	@GetMapping("/getLink")
	public ResponseEntity<ResponseMessage> getLink(@Parameter(description = "Project PK 값") @RequestParam(name = "id") long projectId){
		return ResponseEntity.ok().body(inviteService.getLink(projectId));
	}
	
	/**
	 * 이메일로 초대링크 전송
	 * @param projectId
	 * @return
	 */
	@Operation(summary = "프로젝트 이메일로 초대하기", description = "이메일로 프로젝트 초대장을 전송합니다.")
	@GetMapping("/emailLink")
	public ResponseEntity<ResponseMessage> emailLink(@Parameter(description = "Project PK 값") @RequestParam(name = "id") long projectId, 
													 @Parameter(description = "전송 할 email") @RequestParam(name = "email") String email){
		return ResponseEntity.ok().body(inviteService.emailLink(projectId, email));
	}
}
