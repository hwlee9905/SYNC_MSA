package com.simple.book.domain.user.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simple.book.domain.user.entity.UserEntity;
import com.simple.book.domain.user.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenManagementService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return userRepository.findById(userId)
				.map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
	}
	
	private UserDetails createUserDetails(UserEntity entity) {
		return User.builder()
                .username(entity.getId())
                .password(entity.getPassword())
                .build();
	}
	
	public HashMap<String, Object> logout(HttpSession session){
		HashMap<String, Object> result = new HashMap<>();
		session.invalidate();
		result.put("result", true);
		return result;
	}
}
