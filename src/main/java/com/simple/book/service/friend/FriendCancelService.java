package com.simple.book.service.friend;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.repository.FriendReqRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class FriendCancelService {

	@Autowired
	private FriendReqRepository friendReqRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> requestCancle(HttpSession session, String reqId) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		// 로그인 상태 확인
		if (id != null) {
			// 내가 상태한테 보낸 요청
			Optional<FriendReqEntity> frndOptional = friendReqRepository.findByIdAndReqIdAndAcceptYn((String) id, reqId, "R");
			// 친구 요청이 유효할 경우
			if (frndOptional.isPresent()) {
				FriendReqEntity entity = frndOptional.get();
				friendReqRepository.saveAndFlush(setEntity(entity, (String) id));
				result.put("result", true);
			} else {
				result.put("result", "no_request");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	private FriendReqEntity setEntity(FriendReqEntity entity, String myId) {
		entity.setAcceptYn("C");
		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
		entity.setUpdTime(dateFmt.getDate("HHmmss"));
		entity.setUpdId(myId);
		return entity;
	}
}
