//package com.simple.book.domain.friend.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.simple.book.domain.friend.entity.FriendReqEntity;
//import com.simple.book.domain.friend.repository.FriendReqRepository;
//import com.simple.book.domain.friend.repository.query.QueryFriendReqRepository;
//import com.simple.book.domain.user.repository.UserRepository;
//import com.simple.book.global.util.DateFmt;
//import com.simple.book.global.util.FriendList;
//
//import jakarta.servlet.http.HttpSession;
//
//@Service
//public class FriendService {
//	@Autowired
//	private QueryFriendReqRepository queryFriendReqRepository;
//
//	@Autowired
//	private FriendReqRepository friendReqRepository;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private DateFmt dateFmt;
//
//	@Autowired
//	private FriendList friendList;
//
//	/**
//	 * 친구 신청
//	 * @param session
//	 * @param reqId
//	 * @return
//	 */
//	public HashMap<String, Object> addFriend(HttpSession session, String reqId) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		// 로그인 상태 확인
//		if (id != null) {
//			// 가입 되어 있는 사용자인지 확인
//			boolean userOptional = userRepository.existsById(reqId);
//			if (userOptional) {
//				// 내가 신청 했거나, 신청 받은 요청 목록
//				Optional<FriendReqEntity> reqOptional = queryFriendReqRepository.findByIdOrReqId((String) id, reqId);
//				// 내가 신청 했거나, 신청 받은 적 있음.
//				if (reqOptional.isPresent()) {
//					FriendReqEntity entity = reqOptional.get();
//					// 과거 이력 중, 내가 취소 했거나 상대방이 거절했을 경우
//					if (entity.getAcceptYn().equals("N") || entity.getAcceptYn().equals("C")) {
//						friendReqRepository.saveAndFlush(setEntity(session, (String) id, reqId));
//						result.put("result", true);
//						// 이미 친구 상태
//					} else if (entity.getAcceptYn().equals("Y")) {
//						result.put("result", "friend");
//						// 이미 친구 신청 상태
//					} else {
//						result.put("result", "req_friend");
//					}
//					// 내가 신청 한 적도, 받은 적도 없음.
//				} else {
//					friendReqRepository.saveAndFlush(setEntity(session, (String) id, reqId));
//					result.put("result", true);
//				}
//			} else {
//				result.put("result", "no_req_id");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	/**
//	 * 친구 목록
//	 * @param session
//	 * @param reqId
//	 * @return
//	 */
//	public HashMap<String, Object> findFriend(HttpSession session, String reqId) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		if (id != null) {
//			List<String> list = friendList.getList(session);
//			if (!list.isEmpty()) {
//				for (String friendId : list) {
//					if (friendId.equals(reqId)) {
//
//					}
//				}
//			} else {
//				result.put("result", "no_friend");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	/**
//	 * 친구 수락
//	 * @param session
//	 * @param reqId
//	 * @return
//	 */
//	public HashMap<String, Object> friendAccept(HttpSession session, String reqId) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		// 로그인 상태 확인
//		if (id != null) {
//			// 상대가 나한테 전송한 요청
//			Optional<FriendReqEntity> reqOptional = friendReqRepository.findByIdAndReqIdAndAcceptYn(reqId, (String) id, "R");
//			// 상대방이 나한테 신청 한 이력이 있을 경우
//			if (reqOptional.isPresent()) {
//				FriendReqEntity entity = reqOptional.get();
//				friendReqRepository.saveAndFlush(setEntity(entity, (String) id, "Y"));
//				result.put("result", true);
//			// 상대방이 나한테 신청 한 이력이 없을 경우
//			} else {
//				result.put("result", "no_request");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	/**
//	 * 친구 신청 취소
//	 * @param session
//	 * @param reqId
//	 * @return
//	 */
//	public HashMap<String, Object> requestCancle(HttpSession session, String reqId) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		// 로그인 상태 확인
//		if (id != null) {
//			// 내가 상태한테 보낸 요청
//			Optional<FriendReqEntity> frndOptional = friendReqRepository.findByIdAndReqIdAndAcceptYn((String) id, reqId, "R");
//			// 친구 요청이 유효할 경우
//			if (frndOptional.isPresent()) {
//				FriendReqEntity entity = frndOptional.get();
//				friendReqRepository.saveAndFlush(setEntity(entity, (String) id, "C"));
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
//	/**
//	 * 내 친구 목록
//	 * @param session
//	 * @return
//	 */
//	public HashMap<String, Object> getFriendList(HttpSession session) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		if (id != null) {
//			List<String> list = friendList.getList(session);
//			if (!list.isEmpty()) {
//				result.put("result", list);
//			} else {
//				result.put("result", "no_friend");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	/**
//	 * 친구 거절
//	 * @param session
//	 * @param reqId
//	 * @return
//	 */
//	public HashMap<String, Object> responseReject(HttpSession session, String reqId){
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		// 로그인 상태 확인
//		if (id != null) {
//			// 상대가 나한테 보낸 요청
//			Optional<FriendReqEntity> entity = friendReqRepository.findByIdAndReqIdAndAcceptYn(reqId, (String) id, "R");
//			// 친구 요청이 유효할 경우
//			if (entity.isPresent()) {
//				FriendReqEntity getEntity = entity.get();
//				friendReqRepository.saveAndFlush(setEntity(getEntity, (String) id, "N"));
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 요청한 친구 조회
//	 * @param session
//	 * @return
//	 */
//	public HashMap<String, Object> requestFriend(HttpSession session){
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		if (id != null) {
//			// 내가 상대한테 보낸 모든 요청
//			List<FriendReqEntity> frndEntityList = friendReqRepository.findByIdAndAcceptYn((String) id, "R");
//			// 내가 요청한 친구 목록이 있을 경우
//			if (!frndEntityList.isEmpty()) {
//				List<Object> userList = new ArrayList<>();
//				for (FriendReqEntity entity : frndEntityList) {
//					String toId = entity.getReqId();
//					String insDate = entity.getInsDate();
//					String insTime = entity.getInsTime();
//					userList.add(putRequestInfo(toId, insDate, insTime));
//				}
//				result.put("result", userList);
//			} else {
//				result.put("result", "no_request_friend");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	/**
//	 * 요청받은 친구 조회
//	 * @param session
//	 * @return
//	 */
//	public HashMap<String, Object> responseFriend(HttpSession session) {
//		HashMap<String, Object> result = new HashMap<>();
//		Object id = session.getAttribute("id");
//		// 로그인 상태 확인
//		if (id != null) {
//			// 상대가 나한테 보낸 모든 요청
//			String reqId = (String) id;
//			List<FriendReqEntity> frndEntityList = friendReqRepository.findByReqIdAndAcceptYn(reqId, "R");
//			// 내가 요청한 친구 목록이 있을 경우
//			if (!frndEntityList.isEmpty()) {
//				List<Object> userList = new ArrayList<>();
//				for (FriendReqEntity entity : frndEntityList) {
//					String fromId = entity.getId();
//					String insDate = entity.getInsDate();
//					String insTime = entity.getInsTime();
//					userList.add(putResponseInfo(fromId, insDate, insTime));
//				}
//				result.put("result", userList);
//			} else {
//				result.put("result", "no_request_friend");
//			}
//		} else {
//			result.put("result", "no_session");
//		}
//		return result;
//	}
//
//	private HashMap<String, Object> putResponseInfo(String fromId, String insDate, String insTime){
//		HashMap<String, Object> result = new HashMap<>();
//		result.put("from_id", fromId);
//		result.put("ins_date", insDate);
//		result.put("ins_time", insTime);
//		return result;
//	}
//
//	private HashMap<String, Object> putRequestInfo(String toId, String insDate, String insTime) {
//		HashMap<String, Object> result = new HashMap<>();
//		result.put("to_id", toId);
//		result.put("ins_date", insDate);
//		result.put("ins_time", insTime);
//		return result;
//	}
//
//	private FriendReqEntity setEntity(FriendReqEntity entity, String id, String aceepYn) {
//		entity.setAcceptYn(aceepYn);
//		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
//		entity.setUpdTime(dateFmt.getDate("HHmmss"));
//		entity.setUpdId(id);
//		return entity;
//	}
//
//	private FriendReqEntity setEntity(HttpSession session, String id, String reqId) {
//		FriendReqEntity entity = new FriendReqEntity();
//		String date = dateFmt.getDate("yyyyMMdd");
//		String time = dateFmt.getDate("HHmmss");
//		entity.setId(id);
//		entity.setReqId(reqId);
//		entity.setAcceptYn("R");
//		entity.setInsDate(date);
//		entity.setInsTime(time);
//		entity.setInsId(id);
//		entity.setUpdDate(date);
//		entity.setUpdTime(time);
//		entity.setUpdId(id);
//		return entity;
//	}
//}
