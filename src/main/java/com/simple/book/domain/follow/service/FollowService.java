//package com.simple.book.domain.follow.service;
//
//import java.util.HashMap;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.simple.book.domain.follow.entity.FollowReqEntity;
//import com.simple.book.domain.follow.repository.FollowReqRepository;
//import com.simple.book.domain.user.repository.UserRepository;
//import com.simple.book.global.util.DateFmt;
//
//import jakarta.servlet.http.HttpSession;
//
//@Service
//public class FollowService {
//	@Autowired
//	private FollowReqRepository followReqRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private DateFmt dateFmt;
//
//	/**
//	 * 팔로잉
//	 * @param session
//	 * @param body
//	 * @return
//	 */
//	public HashMap<String, Object> follow(HttpSession session, HashMap<String, Object> body) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		if (id != null) {
//			String reqId = String.valueOf(body.get("req_id"));
//			boolean existsId = userRepository.existsById(reqId);
//			if (existsId) {
//				followReqRepository.saveAndFlush(setEntity(String.valueOf(id), reqId));
//				result.put("result", true);
//			} else {
//				result.put("result", "no_req_id");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//
//		return result;
//	}
//
//	/**
//	 * 언팔로우
//	 * @param session
//	 * @param body
//	 * @return
//	 */
//	public HashMap<String, Object> cancelFollow(HttpSession session, HashMap<String, Object> body) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		if (id != null) {
//			String reqId = String.valueOf(body.get("req_id"));
//			Optional<FollowReqEntity> followOptional = followReqRepository.findByIdAndReqIdAndAcceptYn(String.valueOf(id), reqId, "Y");
//			if (followOptional.isPresent()) {
//				FollowReqEntity entity = followOptional.get();
//				followReqRepository.saveAndFlush(setEntity(entity));
//				result.put("result", true);
//			} else {
//				result.put("result", "no_request");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	private FollowReqEntity setEntity(FollowReqEntity entity) {
//		String date = dateFmt.getDate("yyyyMMdd");
//		String time = dateFmt.getDate("HHmmss");
//		entity.setAcceptYn("N");
//		entity.setUpdDate(date);
//		entity.setUpdTime(time);
//		return entity;
//	}
//
//	private FollowReqEntity setEntity(String id, String reqId) {
//		FollowReqEntity entity = new FollowReqEntity();
//		String date = dateFmt.getDate("yyyyMMdd");
//		String time = dateFmt.getDate("HHmmss");
//		entity.setId(id);
//		entity.setReqId(reqId);
//		entity.setAcceptYn("Y");
//		entity.setInsDate(date);
//		entity.setInsTime(time);
//		entity.setInsId(id);
//		entity.setUpdDate(date);
//		entity.setUpdTime(time);
//		entity.setUpdId(id);
//		return entity;
//	}
//}
