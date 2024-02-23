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
public class FriendRejectService {
	@Autowired
	private FriendReqRepository friendReqRepository;
	
	@Autowired
	private DateFmt dateFmt;
	
	public HashMap<String, Object> responseReject(HttpSession session, String reqId){
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		// 로그인 상태 확인
		if (id != null) {
			// 상대가 나한테 보낸 요청
			Optional<FriendReqEntity> entity = friendReqRepository.findByIdAndReqIdAndAcceptYn(reqId, (String) id, "R");
			// 친구 요청이 유효할 경우
			if (entity.isPresent()) {
				FriendReqEntity getEntity = entity.get();
				friendReqRepository.saveAndFlush(setEntity(getEntity, (String) id));
			}
		}
		return result;
	}
	
	private FriendReqEntity setEntity(FriendReqEntity entity, String id) {
		entity.setAcceptYn("N");
		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
		entity.setUpdTime(dateFmt.getDate("HHmmss"));
		entity.setUpdId(id);
		return entity;
	}
}
