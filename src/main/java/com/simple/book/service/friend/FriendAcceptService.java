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
public class FriendAcceptService {

	@Autowired
	private FriendReqRepository friendReqRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> friendAccept(HttpSession session, String reqId) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		// 로그인 상태 확인
		if (id != null) {
			// 상대가 나한테 전송한 요청
			Optional<FriendReqEntity> reqOptional = friendReqRepository.findByIdAndReqIdAndAcceptYn(reqId, (String) id, "R");
			// 상대방이 나한테 신청 한 이력이 있을 경우
			if (reqOptional.isPresent()) {
				FriendReqEntity entity = reqOptional.get();
				friendReqRepository.saveAndFlush(setEntity(entity, (String) id));
				result.put("result", true);
			// 상대방이 나한테 신청 한 이력이 없을 경우
			} else {
				result.put("result", "no_request");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	private FriendReqEntity setEntity(FriendReqEntity entity, String id) {
		entity.setAcceptYn("Y");
		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
		entity.setUpdTime(dateFmt.getDate("HHmmss"));
		entity.setUpdId(id);
		return entity;
	}
}
