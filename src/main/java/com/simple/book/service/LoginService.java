package com.simple.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simple.book.entity.UserEntity;
import com.simple.book.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		System.out.println("!!LoginService 진입!!");
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
	
	
	/*
	 * 파업 코드
	 */
//	public HashMap<String, Object> login(HashMap<String, Object> body, HttpSession session){
//		HashMap<String, Object> result = new HashMap<>();
//		
//		
////		String status = "false";
//		
//		String id = String.valueOf(body.get("id"));
//		String password = String.valueOf(body.get("password"));
//		Optional<UserEntity> userEntity = userRepository.findByIdAndPassword(id, password);
//		
//		// ID 있을 때
//		if (!userEntity.isPresent()) {
//			UserEntity entity = userEntity.get();
//			setSession(session, entity);
//			status = id;
//		} else {
//			
//		}
//		result.put("result", status);
//		return result;
//	}
//
//	private void setSession(HttpSession session, UserEntity entity) {
//		session.setAttribute("id", entity.getId());
//		session.setAttribute("firstName", entity.getFirstName());
//		session.setAttribute("lastName", entity.getLastName());
//		session.setAttribute("birth", entity.getBirth());
//		session.setAttribute("gender", entity.getGender());
//		session.setAttribute("insDate", entity.getInsDate());
//		session.setAttribute("insTime", entity.getInsTime());
//		session.setAttribute("insId", entity.getInsId());
//		session.setAttribute("updDate", entity.getUpdDate());
//		session.setAttribute("updTime", entity.getUpdTime());
//		session.setAttribute("updId", entity.getUpdId());
//	}

}
