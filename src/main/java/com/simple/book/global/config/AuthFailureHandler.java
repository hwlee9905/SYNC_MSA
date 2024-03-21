package com.simple.book.global.config;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
		String errorMsg = null;
		
		if (exception instanceof BadCredentialsException) {
			errorMsg = "아이디와 비밀번호를 확인해주세요.";
		} else if (exception instanceof InternalAuthenticationServiceException) {
			errorMsg = "내부 시스템 문제로 로그인할 수 없습니다. 관리자에게 문의하세요.";
		} else if (exception instanceof UsernameNotFoundException) {
			errorMsg = "존재하지 않는 계정입니다.";
		} else {
			errorMsg = "알 수 없는 오류 입니다.";
		}
		
		errorMsg = URLEncoder.encode(errorMsg, "UTF-8");
		setDefaultFailureUrl("/login?error=" + errorMsg);
		super.onAuthenticationFailure(request, response, exception);
	}
}
