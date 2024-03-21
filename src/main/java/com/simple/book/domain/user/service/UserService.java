package com.simple.book.domain.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simple.book.domain.friend.entity.FriendReqEntity;
import com.simple.book.domain.friend.repository.FriendReqRepository;
import com.simple.book.domain.user.entity.DelUserEntity;
import com.simple.book.domain.user.entity.UserEntity;
import com.simple.book.domain.user.repository.DelUserRepository;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.domain.user.repository.query.QueryUserRepository;
import com.simple.book.global.util.DateFmt;
import com.simple.book.global.util.ValidationUserInfo;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
	@Autowired
	private QueryUserRepository queryUserRepository;
	
	private final String SUCCESS = "success";

	private String id, password, email, firstName, lastName, birth, gender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DateFmt dateFmt;

	@Autowired
	private ValidationUserInfo validationUserInfo;

	@Autowired
	private DelUserRepository delUserRepository;

	@Autowired
	private FriendReqRepository friendReqRepository;

	/**
	 * 유저 검색
	 * @param session
	 * @param body
	 * @return
	 */
	public HashMap<String, Object> findUser(HttpSession session, HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {
			String category = (String) body.get("category");
			String keyword = (String) body.get("keyword");
			List<UserEntity> userEntityList = null;
			// id 검색
			if (category.equals("id")) {
				userEntityList = queryUserRepository.findByIdLikeKeyword("%" + keyword + "%");
				System.out.println("id: " + userEntityList.toString());
			}
			// 이름 검색
			if (category.equals("name")) {
				userEntityList = queryUserRepository.findByNameLikeKeyword("%" + keyword + "%");
				System.out.println("name: " + userEntityList.toString());
			}
			if (!userEntityList.isEmpty()) {
				result.put("result", userEntityList);
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}
	
	/**
	 * 회원 가입
	 * @param body
	 * @return
	 */
	public HashMap<String, Object> signup(HashMap<String, Object> body) {
		String key = "result";
		HashMap<String, Object> result = new HashMap<>();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		id = String.valueOf(body.get("id"));
		password = passwordEncoder.encode(String.valueOf(body.get("password")));
		email = String.valueOf(body.get("email"));
		firstName = String.valueOf(body.get("first_name"));
		lastName = String.valueOf(body.get("last_name"));
		birth = (String.valueOf(body.get("birth")).replaceAll("-", ""));
		gender = String.valueOf(body.get("gender"));

		String resultId = validationUserInfo.getResultId(id);
		String resultPassword = validationUserInfo.getResultPassword(password);
		String resultEmail = validationUserInfo.getResultEmail(email);
		String resultFirstName = validationUserInfo.getResultFirstName(firstName);
		String resultLastName = validationUserInfo.getResultLastName(lastName);
		String resultBirth = validationUserInfo.getResultBirth(birth);
		String resultGender = validationUserInfo.getResultGender(gender);

		if (!resultId.equals(SUCCESS)) {
			result.put(key, resultId);
		} else if (!resultPassword.equals(SUCCESS)) {
			result.put(key, resultPassword);
		} else if (!resultEmail.equals(SUCCESS)) {
			result.put(key, resultEmail);
		} else if (!resultFirstName.equals(SUCCESS)) {
			result.put(key, resultFirstName);
		} else if (!resultLastName.equals(SUCCESS)) {
			result.put(key, resultLastName);
		} else if (!resultBirth.equals(SUCCESS)) {
			result.put(key, resultBirth);
		} else if (!resultGender.equals(SUCCESS)) {
			result.put(key, resultGender);
		} else {
			String date = dateFmt.getDate("yyyyMMdd");
			String time = dateFmt.getDate("HHmmss");
			UserEntity entity = setEntity(date, time);

			userRepository.saveAndFlush(entity);
			result.put("result", SUCCESS);
		}
		return result;
	}

	/**
	 * 회원정보 수정
	 * @param body
	 * @param session
	 * @return
	 */
	public HashMap<String, Object> modifyUser(HashMap<String, Object> body, HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();

		firstName = (String) body.get("first_name");
		lastName = (String) body.get("last_name");
		password = (String) body.get("password");
		birth = (String) body.get("birth");
		gender = (String) body.get("gender");

		String resultFirstName = validationUserInfo.getResultFirstName(firstName);
		String resultLastName = validationUserInfo.getResultLastName(lastName);
		String resultPassword = validationUserInfo.getResultPassword(password);
		String resultBirth = validationUserInfo.getResultBirth(birth);
		String resultGender = validationUserInfo.getResultGender(gender);

		if (session.getAttribute("id") == null) {
			result.put("result", "no_session");
		} else {
			if (!resultFirstName.equals(SUCCESS)) {
				result.put("result", resultFirstName);
			} else if (!resultLastName.equals(SUCCESS)) {
				result.put("result", resultLastName);
			} else if (!resultPassword.equals(SUCCESS)) {
				result.put("result", resultPassword);
			} else if (!resultBirth.equals(SUCCESS)) {
				result.put("result", resultBirth);
			} else if (!resultGender.equals(SUCCESS)) {
				result.put("result", resultGender);
			} else {
				String date = dateFmt.getDate("YYYYMMdd");
				String time = dateFmt.getDate("HHmmss");
				UserEntity entity = setEntity(date, time, session);

				userRepository.saveAndFlush(entity);
				result.put("result", SUCCESS);
			}
		}

		return result;

	}
	
	/**
	 * 회원 탈퇴
	 * @param session
	 * @return
	 */
	public HashMap<String, Object> deleteUser(HttpSession session) {
		boolean isStateCheck = false;
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		// 로그인 상태 확인
		if (id != null) {
			Optional<UserEntity> userOptional = userRepository.findById((String) id);
			// 계정이 있는지 확인
			if (userOptional.isPresent()) {
				String reqId = (String) id;
				List<FriendReqEntity> frndEntityList = friendReqRepository.findByIdOrReqId((String) id, reqId);
				UserEntity userEntity = userOptional.get();
				delUserRepository.saveAndFlush(setDelEntity(userEntity));
				userRepository.deleteById((String) id);
				// 내가 요청하거나 받은 친구 상태가 있는지
				if (!frndEntityList.isEmpty()) {
					for (FriendReqEntity entity : frndEntityList) {
						friendReqRepository.saveAndFlush(setFrndEntity(entity, (String) id));
						session.invalidate();
						isStateCheck = true;
					}
				}
				session.invalidate();
				isStateCheck = true;
			}
		}
		result.put("result", isStateCheck);
		return result;
	}
	
	private UserEntity setEntity(String date, String time) {
		UserEntity entity = new UserEntity();

		entity.setId(id);
		entity.setPassword(password);
		entity.setEmail(email);
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setBirth(birth);
		entity.setGender(gender);
		entity.setDelYn("N");
		entity.setInsDate(date);
		entity.setInsTime(time);
		entity.setInsId(id);
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId(id);

		return entity;
	}
	
	private DelUserEntity setDelEntity(UserEntity userEntity) {
		DelUserEntity delUserEntity = new DelUserEntity();
		delUserEntity.setId(userEntity.getId());
		delUserEntity.setFirstName(userEntity.getFirstName());
		delUserEntity.setLastName(userEntity.getLastName());
		delUserEntity.setPassword(userEntity.getPassword());
		delUserEntity.setBirth(userEntity.getBirth());
		delUserEntity.setGender(userEntity.getGender());
		delUserEntity.setDelYn("Y");
		delUserEntity.setInsDate(userEntity.getInsDate());
		delUserEntity.setInsTime(userEntity.getInsTime());
		delUserEntity.setInsId(userEntity.getInsId());
		delUserEntity.setUpdDate(userEntity.getUpdDate());
		delUserEntity.setUpdTime(userEntity.getUpdTime());
		delUserEntity.setUpdId(userEntity.getUpdId());
		return delUserEntity;
	}

	private FriendReqEntity setFrndEntity(FriendReqEntity entity, String id) {
		entity.setAcceptYn("N");
		entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
		entity.setUpdTime(dateFmt.getDate("HHmmss"));
		entity.setUpdId(id);
		return entity;
	}

	private UserEntity setEntity(String date, String time, HttpSession session) {
		UserEntity entity = new UserEntity();

		entity.setId((String) session.getAttribute("id"));
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		entity.setPassword(password);
		entity.setBirth(birth);
		entity.setGender(gender);
		entity.setDelYn("N");
		entity.setInsDate((String) session.getAttribute("insDate"));
		entity.setInsTime((String) session.getAttribute("insTime"));
		entity.setInsId((String) session.getAttribute("insId"));
		entity.setUpdDate(date);
		entity.setUpdTime(time);
		entity.setUpdId((String) session.getAttribute("id"));

		return entity;
	}
}
