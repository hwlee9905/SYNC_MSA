package com.simple.book.service.friend;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.repository.FriendReqRepository;
import com.simple.book.util.FriendList;

import jakarta.servlet.http.HttpSession;

@Service
public class FindFriendService {

	@Autowired
	private FriendReqRepository friendReqRepository;
	
	@Autowired
	private FriendList friendList;

	public HashMap<String, Object> findFriend(HttpSession session, String reqId) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {
			List<String> list = friendList.getList(session);
			if (!list.isEmpty()) {
				for (String friendId : list) {
					if (friendId.equals(reqId)) {
						
					}
				}
			} else {
				result.put("result", "no_friend");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}
}
