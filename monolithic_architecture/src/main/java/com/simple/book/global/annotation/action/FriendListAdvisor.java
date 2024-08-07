package com.simple.book.global.annotation.action;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class FriendListAdvisor {
	@Before("@annotation(com.simple.book.global.annotation.FriendList)")
	public void friendList() {
	}
}
