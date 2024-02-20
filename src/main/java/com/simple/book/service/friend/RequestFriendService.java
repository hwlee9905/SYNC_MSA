package com.simple.book.service.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.repository.FriendReqRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class RequestFriendService {
	@Autowired
	private FriendReqRepository friendReqRepository;

	public HashMap<String, Object> requestFriend(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			List<FriendReqEntity> entityList = friendReqRepository.findByIdAndAcceptYn((String) myId, "R");
			if (entityList.size() != 0) {
				List<Object> userList = new ArrayList<>();
				for (FriendReqEntity entity : entityList) {
					String toId = entity.getReqId();
					String insDate = entity.getInsDate();
					String insTime = entity.getInsTime();
					userList.add(putUserInf(toId, insDate, insTime));
				}
				result.put("result", userList);
			} else {
				result.put("result", "no_request_friend");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}
	
	public HashMap<String, Object> putUserInf(String toId, String insDate, String insTime) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("to_id", toId);
		result.put("ins_date", insDate);
		result.put("ins_time", insTime);
		return result;
	}
}
