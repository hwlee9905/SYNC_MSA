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
	private DateFmt dateFmt;

	public HashMap<String, Object> deleteId(HttpSession session) {
		HashMap<String, Object> result = new HashMap<>();
		Object myId = session.getAttribute("id");
		if (myId != null) {
			Optional<UserEntity> userInfo = userRepository.findById((String) myId);
			if (userInfo.isPresent()) {
				List<FriendReqEntity> frndEntity = friendReqRepository.findById((String) myId);
				UserEntity userEntity = userInfo.get();
				delUserRepository.saveAndFlush(setDelEntity(userEntity));
				userRepository.deleteById((String) myId);
				friendReqRepository.saveAll(setFrndEntity(frndEntity, (String) myId));
				session.invalidate();
				result.put("result", true);
			} else {
				result.put("result", false);
			}
		} else {
			result.put("result", false);
		}
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

	private List<FriendReqEntity> setFrndEntity(List<FriendReqEntity> frndEntity, String id) {
		for (FriendReqEntity entity : frndEntity) {
			entity.setAcceptYn("N");
			entity.setUpdDate(dateFmt.getDate("yyyyMMdd"));
			entity.setUpdTime(dateFmt.getDate("HHmmss"));
			entity.setUpdId(id);
		}
		return frndEntity;
	}
}
