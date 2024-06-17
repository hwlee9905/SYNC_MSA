package com.simple.book.domain.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.project.service.InviteService;
import com.simple.book.global.util.ResponseMessage;

@RestController
@RequestMapping(value = "/api/user/invite", produces = MediaType.APPLICATION_JSON_VALUE)
public class InviteController {
	
	@Autowired
	private InviteService inviteService;

	/**
	 * 테스트용 초대 링크 발급 컨트롤러
	 * @param projectId
	 * @return
	 */
	@PostMapping("/createLink")
	public ResponseEntity<ResponseMessage> createLink(@RequestParam(name = "id") long projectId){
		return ResponseEntity.ok().body(inviteService.createLink(projectId));
	}
	
	/**
	 * 초대링크 가져오기 (얘는 찐으로 쓰는거임)
	 * @param projectId
	 * @return
	 */
	@GetMapping("/getLink")
	public ResponseEntity<ResponseMessage> getLink(@RequestParam(name = "id") long projectId){
		return ResponseEntity.ok().body(inviteService.getLink(projectId));
	}
	
	/**
	 * 이메일로 초대링크 전송
	 * @param projectId
	 * @return
	 */
	@GetMapping("/emailLink")
	public ResponseEntity<ResponseMessage> emailLink(@RequestParam(name = "id") long projectId, @RequestParam(name = "email") String email){
		return ResponseEntity.ok().body(inviteService.emailLink(projectId, email));
	}
}
