package user.service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import user.service.UserService;
import user.service.global.advice.LogAop;
import user.service.web.dto.request.ModifyPwdRequestDto;
import user.service.web.dto.request.ModifyUserInfoRequestDto;
import user.service.global.advice.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/user/api/")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	//.requestMatchers("/user").hasAnyAuthority("USER") USER 계정 로그인 필요
	@Operation(summary = "현재 로그인 유저 정보를 가져오는 API", description = "HOST = 150.136.153.235:30443")
	@GetMapping("info/v1")
	@LogAop
	public SuccessResponse getCurrentUserInfo(){
		return userService.getUserInfo();
	}
	@Operation(summary = "유저들의 정보를 가져오는 API", description = "HOST = 150.136.153.235:30443")
	@GetMapping("info/v2")
	@LogAop
	public SuccessResponse getUsersInfo(@Parameter(description = "존재하지 않는 유저 아이디 입력시 오류 발생") @RequestParam List<Long> userIds){
		return userService.getUsersInfo(userIds);
	}
	@Operation(summary = "유저의 비밀번호를 변경하는 API", description = "HOST = 150.136.153.235:30443")
	@PutMapping("pwd")
	@LogAop
	public ResponseEntity<SuccessResponse> modifyPwd(@Valid @RequestBody ModifyPwdRequestDto body){
		String userId = userService.getCurrentUserId();
		UserDetails userDetails = userService.loadUserByUsername(userId);
		return ResponseEntity.ok().body(userService.modifyPwd(body, userDetails));
	}
	@Operation(summary = "유저의 정보를 변경하는 API", description = "HOST = 150.136.153.235:30443")
	@PutMapping("info")
	@LogAop
	public ResponseEntity<SuccessResponse> modifyUserInfo(@RequestBody ModifyUserInfoRequestDto body) {
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok().body(userService.modifyUserInfo(body, userId));
	}
//	@ResponseBody
//	@DeleteMapping("remove")
//	public ResponseEntity<ResponseMessage> removeUser() {
//		return ResponseEntity.ok().body(userService.remove(userService.getCurrentUserId()));
//	}
}
