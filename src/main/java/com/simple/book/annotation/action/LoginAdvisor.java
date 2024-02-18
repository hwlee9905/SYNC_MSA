package com.simple.book.annotation.action;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class LoginAdvisor {

	@Around("@annotation(com.simple.book.annotation.Login)")
	public void test() {
		
	}

}
