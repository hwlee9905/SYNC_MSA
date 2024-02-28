package com.simple.book.service.user;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.query.QueryUserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class FindUserService {
	@Autowired
	private QueryUserRepository queryUserRepository;

	public HashMap<String, Object> search(HttpSession session, HashMap<String, Object> body) {
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
}
