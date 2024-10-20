package user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import user.service.entity.Authentication;
import user.service.entity.InfoSet;
import user.service.entity.Role;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.repository.AuthenticationRepository;
import user.service.repository.UserRepository;
import user.service.web.dto.UserInfoResponseDtoV1;
import user.service.web.dto.request.SignupRequestDto;

@Slf4j
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private AuthenticationRepository authenticationRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock
	private UserDetails userDetails;
	// 실제 객체 주입
	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	public void testSignup() {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("Test User", "Tester", "testUser", "password", "test@example.com");

		Authentication authentication = Authentication.builder().userId(signupRequestDto.getUserId())
				.email(signupRequestDto.getEmail()).password("encodedPassword").infoSet(InfoSet.DEFAULT).build();

		User user = User.builder().username(signupRequestDto.getUsername()).role(Role.USER)
				.nickname(signupRequestDto.getNickname()).build();
		user.setAuthentication(authentication);
		authentication.setUser(user);

		when(bCryptPasswordEncoder.encode(signupRequestDto.getPassword())).thenReturn("encodedPassword");
		when(authenticationRepository.saveAndFlush(any(Authentication.class))).thenReturn(authentication);

		// Mock UserRepository에 임의의 유저 설정
		User savedUser = User.builder().username(signupRequestDto.getUsername()).role(Role.USER)
				.nickname(signupRequestDto.getNickname()).build();
		savedUser.setId(1L); // 임의 ID 설정
		savedUser.setAuthentication(authentication);
		when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);
		// when
		User result = userService.signup(signupRequestDto);

		// then
		assertNotNull(result);
		assertEquals(signupRequestDto.getUsername(), result.getUsername());
		assertEquals(signupRequestDto.getNickname(), result.getNickname());
		verify(authenticationRepository, times(1)).saveAndFlush(any(Authentication.class));
		verify(userRepository, times(1)).saveAndFlush(any(User.class));
	}
	@Test
	public void testGetCurrentUserInfo() {
		// given
		String userId = "testUser";
		User user = new User();
		user.setId(1L);
		user.setUsername("Test User");
		user.setNickname("Tester");
		user.setPosition("Developer");

		when(userDetails.getUsername()).thenReturn(userId);
		when(userRepository.findByAuthenticationUserId(userId)).thenReturn(user);

		// when
		SuccessResponse response = userService.getUserInfo();

		// then
		assertNotNull(response);
		assertEquals("유저 정보 조회 성공", response.getMessage());

		UserInfoResponseDtoV1 userInfo = (UserInfoResponseDtoV1) response.getData();
		assertNotNull(userInfo);
		assertEquals("Test User", userInfo.getUsername());
		assertEquals("Tester", userInfo.getNickname());
		assertEquals("Developer", userInfo.getPosition());
	}
}
