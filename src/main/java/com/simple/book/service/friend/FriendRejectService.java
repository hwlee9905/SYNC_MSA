package com.simple.book.service.friend;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.repository.FriendReqRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class FriendRejectService {
	@Autowired
	private FriendReqRepository friendReqRepository;
	
	public HashMap<String, Object> responseReject(HttpSession session, String id){
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			int reqNo = friendReqRepository.findByIdAndReqIdAndAcceptYn((String) myId, id, "R");
		}
		return result;
	}
}
