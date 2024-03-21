package com.simple.book.global.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.book.domain.friend.repository.query.QueryFriendReqRepository;

import jakarta.servlet.http.HttpSession;

@Component
public class FriendList {
	@Autowired
	private QueryFriendReqRepository queryFriendReqRepository;

	public List<String> getList(HttpSession session) {
		String id = (String) session.getAttribute("id");
		List<String> frndEntityList = queryFriendReqRepository.findByIdAndAcceptYn((String) id, "Y");
		return frndEntityList;
	}
}
