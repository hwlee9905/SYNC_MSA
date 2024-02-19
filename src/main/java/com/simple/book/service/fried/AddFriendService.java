package com.simple.book.service.fried;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.entity.UserEntity;
import com.simple.book.repository.FriendReqRepository;
import com.simple.book.repository.UserRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class AddFriendService {

	@Autowired
	private FriendReqRepository friendReqRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> addFriend(HttpSession session, String id) {
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			Optional<UserEntity> userOptional = userRepository.findById(id);
			if (userOptional.isPresent()) {
				FriendReqEntity entity = friendReqRepository.findByIdAndReqId((String) myId, id);
				if (entity == null || entity.getAcceptYn().equals("N") || entity.getAcceptYn().equals("C")) {
					friendReqRepository.saveAndFlush(setEntity(session, (String) myId, id));
					result.put("result", true);
				} else if (entity.getAcceptYn().equals("Y")) {
					result.put("result", "friend");
				} else {
					result.put("result", "req_friend");
				}
			} else {
				result.put("result", "no_req_id");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	private FriendReqEntity setEntity(HttpSession session, String myId, String id) {
		FriendReqEntity entity = new FriendReqEntity();
		String date = dateFmt.getDate("yyyyMMdd");
		String time = dateFmt.getDate("HHmmss");
		entity.setId(myId);
		entity.setReqId(id);
		entity.setAcceptYn("R");
		entity.setInsDate(date);
		entity.setInsTime(time);
		entity.setInsId(myId);
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId(myId);
		return entity;
	}
}
