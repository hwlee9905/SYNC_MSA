package alarm.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alarm.service.AlarmService;
import alarm.service.global.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {
	private final AlarmService alarmService;
	
	@GetMapping("/hisotry")
	public ResponseEntity<SuccessResponse> getAlarmHistory(@RequestParam(name="userId") long userId){
		return ResponseEntity.ok(alarmService.getAlarmHistory(userId));
	}

}
