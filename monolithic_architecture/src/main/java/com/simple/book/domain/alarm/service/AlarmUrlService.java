package com.simple.book.domain.alarm.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.simple.book.domain.alarm.dto.AlarmUrlDto;
import com.simple.book.domain.alarm.repository.AlarmUrlRepository;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.global.advice.ResponseMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmUrlService {
	private final UserRepository userRepository;
	private final AlarmUrlRepository alarmUrlRepository;
	
	public ResponseMessage getAlarmUrl(String userId) {
		String result = null;
		if (userId != null) {
			Optional<UUID> oldUrl = alarmUrlRepository.findUrlByAuthenticationUserId(userId);
			if (oldUrl.isPresent()) {
				result = "/topic/user/" + oldUrl.get();
			} else {
				throw new RuntimeException("시스템 오류가 발생하였습니다.");
			}
		} else {
			throw new RuntimeException("로그인 후 이용해 주시길 바랍니다.");
		}
		return ResponseMessage.builder().value(result).build();
	}

	public void createAlarmUrl(long id) {
		User user = userRepository.getReferenceById(id);
		AlarmUrlDto dto = AlarmUrlDto.builder().user(user).build();
		try {
			alarmUrlRepository.saveAndFlush(dto.toEntity());
		} catch (Exception e) {
			throw new RuntimeException("시스템 오류가 발생하였습니다.",e);
		}
	}
}
