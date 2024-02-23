package com.simple.book.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.DelUserEntity;
import com.simple.book.entity.FriendReqEntity;
import com.simple.book.entity.UserEntity;
import com.simple.book.repository.DelUserRepository;
import com.simple.book.repository.FriendReqRepository;
import com.simple.book.repository.UserRepository;
import com.simple.book.repository.query.QueryFriendReqRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class DeleteUserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DelUserRepository delUserRepository;

	@Autowired
	private FriendReqRepository friendReqRepository;
	
	@Autowired
	private QueryFriendReqRepository queryFriendReqRepository;

	@Autowired
	private DateFmt dateFmt;

	public HashMap<String, Object> deleteId(HttpSession session) {
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
}
