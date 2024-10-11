package user.service.web;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import user.service.AlarmService;
import user.service.UserService;
import user.service.kafka.alarm.KafkaAlarmProducerService;

@RestController
@RequestMapping(value = "/api/user/alarm", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AlarmController {
	private final AlarmService alarmService;
	private final UserService userService;
	private final KafkaAlarmProducerService kafkaAlarmProducerService;

	private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@Operation(summary = "과거 알림 불러오기", description = "데이터베이스에 저장 된 알림 정보를 불러옵니다.")
	@GetMapping("/list")
	public void reqAlarmList() {
		String loginId = userService.getCurrentUserId();
//		alarmService.reqAlarmList(loginId);
	}

//	@GetMapping(value="/response/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<String> resAlarmList() {
//		Flux<String> result = alarmService.getAlarmList();
//		return result;
//	}

//	@GetMapping(value = "/response/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public SseEmitter resAlarmList() {
//		String loginId = userService.getCurrentUserId();
//		return alarmService.resAlarmList(loginId);
//	}

//
//	@Operation(summary = "과거 알림 닫기", description = "데이터베이스에 저장 된 알림을 삭제 합니다.")
//	@DeleteMapping("/close")
//	public ResponseEntity<ResponseMessage> deleteAlarm(@Parameter(description = "알림 PK 값") @RequestParam(name="alarmId") UUID alarmId){
//		return ResponseEntity.ok().body(alarmService.deleteAlarm(alarmId));
//	}

}
