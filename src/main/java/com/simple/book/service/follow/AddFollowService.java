package com.simple.book.service.follow;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FollowReqEntity;
import com.simple.book.repository.FollowReqRepository;
import com.simple.book.repository.UserRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class AddFollowService {
	@Autowired
	private FollowReqRepository followReqRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> follow(HttpSession session, HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {
			String reqId = String.valueOf(body.get("req_id"));
			boolean existsId = userRepository.existsById(reqId);
			if (existsId) {
				followReqRepository.saveAndFlush(setEntity(String.valueOf(id), reqId));
				result.put("result", true);
			} else {
				result.put("result", "no_req_id");
			}
		} else {
			result.put("result", "no_session");
		}

		return result;
	}

	private FollowReqEntity setEntity(String id, String reqId) {
		FollowReqEntity entity = new FollowReqEntity();
		String date = dateFmt.getDate("yyyyMMdd");
		String time = dateFmt.getDate("HHmmss");
		entity.setId(id);
		entity.setReqId(reqId);
		entity.setAcceptYn("Y");
		entity.setInsDate(date);
		entity.setInsTime(time);
		entity.setInsId(id);
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId(id);
		return entity;
	}
}
