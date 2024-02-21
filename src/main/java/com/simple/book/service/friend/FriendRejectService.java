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
	
	public HashMap<String, Object> responseReject(HttpSession session, String id){
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			Optional<FriendReqEntity> entity = friendReqRepository.findByIdAndReqIdAndAcceptYn((String) myId, id, "R");
			if (entity.isPresent()) {
				FriendReqEntity getEntity = entity.get();
				friendReqRepository.saveAndFlush(setEntity(getEntity, (String) myId));
			}
		}
		return result;
	}
	
	private FriendReqEntity setEntity(FriendReqEntity entity, String myId) {
		entity.setAcceptYn("N");
		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
		entity.setUpdTime(dateFmt.getDate("HHmmss"));
		entity.setUpdId(myId);
		return entity;
	}
}
