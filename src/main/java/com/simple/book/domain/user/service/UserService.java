package com.simple.book.domain.user.service;


import com.simple.book.domain.jwt.dto.AuthTokenDto;
import com.simple.book.domain.jwt.dto.CustomUserDetails;
import com.simple.book.domain.user.dto.request.SignupRequestDto;
import com.simple.book.domain.user.entity.Authentication;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.AuthenticationRepository;
import com.simple.book.domain.user.util.Address;
import com.simple.book.domain.user.util.InfoSet;
import com.simple.book.domain.user.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.simple.book.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	//회원가입
	@Transactional(rollbackFor = {Exception.class})
	public User signup(SignupRequestDto signupRequestDto) {
//		log.info("signup password : " + signupRequestDto.getPassword());
		Authentication authentication = Authentication.builder()
				.userId(signupRequestDto.getUserId())
				.email(signupRequestDto.getEmail())
				.password(bCryptPasswordEncoder.encode(signupRequestDto.getPassword()))
				.infoSet(InfoSet.DEFAULT)
				.build();
		authenticationRepository.save(authentication);
		Address address = Address.builder()
				.city(signupRequestDto.getCity())
				.district(signupRequestDto.getDistrict())
				.roadAddress(signupRequestDto.getRoadAddress())
				.build();
		User user = User.builder()
				.username(signupRequestDto.getUsername())
				.role(Role.USER)
				.nickname(signupRequestDto.getNickname())
				.sex(signupRequestDto.getSex())
				.address(address)
				.build();
		user.setAuthentication(authentication);
		userRepository.save(user);
		return user;
	}
	public User Read(String userId) {
		return userRepository.findByUserId(userId);
	}
	//로그인
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		//DB에서 조회
		User user = userRepository.findByUserId(userId);
		if (user != null) {
			AuthTokenDto authTokenDto = AuthTokenDto.builder()
					.infoSet(user.getAuthentication().getInfoSet().toString())
					.name(user.getUsername())
					.username(user.getAuthentication().getUserId())
					.password(user.getAuthentication().getPassword())
					.role(user.getRole().toString())
					.build();
			//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
			return new CustomUserDetails(authTokenDto);
		}
		return null;
	}

}
