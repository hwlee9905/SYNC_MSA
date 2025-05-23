package user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import user.service.entity.*;
import user.service.global.advice.ErrorCode;
import user.service.global.advice.SuccessResponse;
import user.service.global.exception.AuthenticationFailureException;
import user.service.global.exception.UnknownException;
import user.service.global.exception.UserIdDuplicatedException;
import user.service.global.exception.UserNotFoundException;
import user.service.jwt.dto.AuthTokenDto;
import user.service.jwt.dto.CustomUserDetails;
import user.service.oauth2.CustomOAuth2User;
import user.service.repository.AuthenticationRepository;
import user.service.repository.MemberRepository;
import user.service.repository.UserRepository;
import user.service.web.dto.UserInfoResponseDtoV1;
import user.service.web.dto.UserInfoResponseDtoV2;
import user.service.web.dto.request.ModifyPwdRequestDto;
import user.service.web.dto.request.ModifyUserInfoRequestDto;
import user.service.web.dto.request.SignupRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final AuthenticationRepository authenticationRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MemberRepository memberRepository;
	/**
	 * 사용자 존재 여부 확인
	 * @param userId
	 * @return User
	 * @throws UserNotFoundException
	 */
	@Transactional(rollbackFor = { Exception.class })
	public User findUserEntity(String userId) {
        return Optional.ofNullable(userRepository.findByAuthenticationUserId(userId))
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
	}
	/**
	 * 사용자 삭제
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse remove(String userId) {
		try {
			Authentication authentication = authenticationRepository.findByUserId(userId);
			authenticationRepository.delete(authentication);
		} catch (Exception e) {
			throw new UnknownException(e.getMessage());
		}
		return SuccessResponse.builder().message("success").build();
	}
	/**
	 * 사설 회원가입
	 * @param signupRequestDto
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public User signup(SignupRequestDto signupRequestDto) {
		boolean isSuccess;
		long id;
		Authentication authentication = Authentication.builder().userId(signupRequestDto.getUserId())
				.email(signupRequestDto.getEmail())
				.password(bCryptPasswordEncoder.encode(signupRequestDto.getPassword())).infoSet(InfoSet.DEFAULT)
				.build();
		try {
			authenticationRepository.saveAndFlush(authentication);
			isSuccess = true;
		} catch (DataIntegrityViolationException e) {
			throw new UserIdDuplicatedException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		User user = User.builder()
				.username(signupRequestDto.getUsername())
				.role(Role.USER)
				.nickname(signupRequestDto.getNickname()).build();
		user.setAuthentication(authentication);
		authentication.setUser(user);
		try {
			id = userRepository.saveAndFlush(user).getId();
			isSuccess = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (isSuccess) {
//			alarmUrlService.createAlarmUrl(id);
		}

		return user;
	}
	/**
	 * 사설 로그인
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// DB에서 조회
		Authentication authentication = authenticationRepository.findByUserId(userId);
		if (authentication != null) {
			AuthTokenDto authTokenDto = AuthTokenDto.builder().infoSet(authentication.getInfoSet().toString())
					.name(authentication.getUser().getUsername()).username(authentication.getUserId())
					.password(authentication.getPassword()).role(authentication.getUser().getRole().toString()).build();

			// UserDetails에 담아서 return하면 AutneticationManager가 검증 함
			return new CustomUserDetails(authTokenDto);
		}
		throw new AuthenticationFailureException("아이디가 잘못되었습니다.", ErrorCode.USER_FAILED_AUTHENTICATION);
	}

	/**
	 * 사용자 정보 가져오기
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse getUserInfo() {
		try {
			String id = getCurrentUserId();
			User info = userRepository.findByAuthenticationUserId(id);
			log.info("info : {}", info.getAuthentication().getUserId());
			UserInfoResponseDtoV1 userInfoResponseDto = UserInfoResponseDtoV1.builder()
					.username(info.getUsername())
					.nickname(info.getNickname())
					.position(info.getPosition())
					.userId(info.getId())
					.build();
			return SuccessResponse.builder()
					.message("유저 정보 조회 성공")
					.data(userInfoResponseDto).build();
		} catch (Exception e) {
			throw new UnknownException(e.getMessage());
		}
	}
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse getUsersInfo(List<Long> userIds) {
		List<UserInfoResponseDtoV2> userInfoList = userIds.stream()
			.map(this::findById)
			.map(user -> {
                return UserInfoResponseDtoV2.builder()
					.username(user.getUsername())
					.nickname(user.getNickname())
					.position(user.getPosition())
					.userId(user.getAuthentication().getUserId())
					.build();
			})
			.collect(Collectors.toList());
		return SuccessResponse.builder()
				.message("유저 정보 조회 성공")
				.data(userInfoList)
				.build();
	}
	/**
	 * 정보 변경
	 * @param body
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse modifyUserInfo(ModifyUserInfoRequestDto body, String userId) {
		User user = userRepository.findByAuthenticationUserId(userId);
		if (user == null) {
			throw new UserNotFoundException("User not found with ID: " + userId);
		}

		if (body.getUsername() != null) {
			user.setUsername(body.getUsername());
		}
		if (body.getNickname() != null) {
			user.setNickname(body.getNickname());
		}
		if (body.getPosition() != null) {
			user.setPosition(body.getPosition());
		}
		if (body.getIntroduction() != null) {
			user.setIntroduction(body.getIntroduction());
		}

		userRepository.save(user);

		return SuccessResponse.builder().message("수정 완료.").build();
	}
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse modifyPwd(ModifyPwdRequestDto body, UserDetails userDetails) {
		SuccessResponse result = null;
		String encodedPassword = userDetails.getPassword();
		boolean isCurrenPwdMatch = bCryptPasswordEncoder.matches(body.getCurrentPwd(), encodedPassword);
		if (isCurrenPwdMatch) {
			if (body.getNewPwd().equals(body.getCheckNewPwd())) {
				Authentication auth = authenticationRepository.findByUserId(userDetails.getUsername());
				auth.setPassword(bCryptPasswordEncoder.encode(body.getNewPwd()));
				authenticationRepository.saveAndFlush(auth);
				result = SuccessResponse.builder().message("success").build();
			} else {
				result = SuccessResponse.builder().message("비밀번호가 일치 하지 않습니다.").build();
			}
		} else {
			result = SuccessResponse.builder().message("비밀번호를 확인 해 주세요.").build();
		}
		return result;
	}
	public String getCurrentUserId() {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			if (authentication instanceof OAuth2AuthenticationToken) {
				CustomOAuth2User oauthToken = (CustomOAuth2User) authentication.getPrincipal();
				return oauthToken.getUsername(); // OAuth2로 인증된 경우 사용자 ID 추출
			} else if (authentication instanceof UsernamePasswordAuthenticationToken) {
				CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
				return customUserDetails.getUsername();
			}
		}
		return "Guest"; // 사용자가 인증되지 않았거나 인증 정보가 없는 경우
	}
	public User findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
	}
	public Long getUserEntityId(String userId) {
		User user = userRepository.findByAuthenticationUserId(userId);
		return user.getId();
	}
}
