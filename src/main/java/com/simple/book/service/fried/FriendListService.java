package com.simple.book.service.fried;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.repository.FriendReqRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class FriendListService {
	@Autowired
	private FriendReqRepository friendReqRepository;
	public HashMap<String, Object> getList(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			List<FriendReqEntity> frndEntity = friendReqRepository.findByIdAndAcceptYn((String) myId, "Y");
			if (frndEntity != null) {
				List<String> list = new ArrayList<>();
				for (FriendReqEntity entity : frndEntity) {
					String friend = "";
					if (entity.getId().equals((String)myId)) {
						friend = entity.getReqId();
					} else {
						friend = entity.getId();
					}
					list.add(friend);
				}
				result.put("result", list);
			} else {
				result.put("result", "no_friend");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}
}
