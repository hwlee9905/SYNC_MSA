package com.simple.book.service.follow;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FollowReqEntity;
import com.simple.book.repository.FollowReqRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class CancelFollowService {
	@Autowired
	private FollowReqRepository followReqRepository;
	
	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> cancelFollow(HttpSession session, HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {
			String reqId = String.valueOf(body.get("req_id"));
			Optional<FollowReqEntity> followOptional = followReqRepository.findByIdAndReqIdAndAcceptYn(String.valueOf(id), reqId, "Y");
			if (followOptional.isPresent()) {
				FollowReqEntity entity = followOptional.get();
				followReqRepository.saveAndFlush(setEntity(entity));
				result.put("result", true);
			} else {
				result.put("result", "no_request");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}
	
	private FollowReqEntity setEntity(FollowReqEntity entity) {
		String date = dateFmt.getDate("yyyyMMdd");
		String time = dateFmt.getDate("HHmmss");
		entity.setAcceptYn("N");
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		return entity;
	}
}
