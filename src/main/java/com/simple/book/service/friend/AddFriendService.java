package com.simple.book.service.friend;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.FriendReqEntity;
import com.simple.book.entity.UserEntity;
import com.simple.book.repository.FriendReqRepository;
import com.simple.book.repository.UserRepository;
import com.simple.book.repository.query.QueryFriendReqRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class AddFriendService {

	@Autowired
	private QueryFriendReqRepository queryFriendReqRepository;
	
	@Autowired
	private FriendReqRepository friendReqRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> addFriend(HttpSession session, String reqId) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		// 로그인 상태 확인
		if (id != null) {
			// 가입 되어 있는 사용자인지 확인
			Optional<UserEntity> userOptional = userRepository.findById(reqId);
			if (userOptional.isPresent()) {
				// 내가 신청 했거나, 신청 받은 요청 목록
				Optional<FriendReqEntity> reqOptional = queryFriendReqRepository.findByIdOrReqId((String) id, reqId);
				// 내가 신청 했거나, 신청 받은 적 있음.
				if (reqOptional.isPresent()) {
					FriendReqEntity entity = reqOptional.get();
					// 과거 이력 중, 내가 취소 했거나 상대방이 거절했을 경우
					if (entity.getAcceptYn().equals("N") || entity.getAcceptYn().equals("C")) {
						friendReqRepository.saveAndFlush(setEntity(session, (String) id, reqId));
						result.put("result", true);
						// 이미 친구 상태
					} else if (entity.getAcceptYn().equals("Y")) {
						result.put("result", "friend");
						// 이미 친구 신청 상태
					} else {
						result.put("result", "req_friend");
					}
					// 내가 신청 한 적도, 받은 적도 없음.
				} else {
					friendReqRepository.saveAndFlush(setEntity(session, (String) id, reqId));
					result.put("result", true);
				}
			} else {
				result.put("result", "no_req_id");
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	private FriendReqEntity setEntity(HttpSession session, String id, String reqId) {
		FriendReqEntity entity = new FriendReqEntity();
		String date = dateFmt.getDate("yyyyMMdd");
		String time = dateFmt.getDate("HHmmss");
		entity.setId(id);
		entity.setReqId(reqId);
		entity.setAcceptYn("R");
		entity.setInsDate(date);
		entity.setInsTime(time);
		entity.setInsId(id);
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId(id);
		return entity;
	}
}
