package user.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import user.service.entity.InfoSet;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.jwt.dto.AuthTokenDto;
import user.service.jwt.dto.CustomUserDetails;
import user.service.repository.UserRepository;
import user.service.web.dto.UserInfoResponseDtoV1;
import user.service.web.dto.UserInfoResponseDtoV2;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Slf4j
@SpringBootTest
@Transactional
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeEach
	void setUp() {
		String userId = "sldjfrnfzjvl";
		String role = "USER"; // 필요에 따라 적절한 역할 설정
		String infoSet = InfoSet.DEFAULT.toString();
		String name = "sldjfrnfzjvl";

		// 테스트용 UserDetails 객체 생성
		AuthTokenDto user = AuthTokenDto.builder()
				.infoSet(infoSet)
				.username(userId)
				.name(name)
				.role(role)
				.password("qwe123!@#")
				.build();

		CustomUserDetails customUserDetails = new CustomUserDetails(user);

		// Spring Security 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

		// SecurityContext에 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	@Test
	public void LongIds_유저들정보가져오기() {
		// given
		user.service.entity.Authentication authentication = user.service.entity.Authentication.builder().userId("test1")
				.email("test@test.com")
				.password(bCryptPasswordEncoder.encode("test123!@#"))
				.infoSet(InfoSet.DEFAULT)
				.build();

		User user1 = User.builder()
				.id(64L)
				.username("testUser64")
				.nickname("nickname64")
				.authentication(authentication)
				.build();
		userRepository.save(user1);

		user.service.entity.Authentication authentication2 = user.service.entity.Authentication.builder().userId("test2")
				.email("test@test.com")
				.password(bCryptPasswordEncoder.encode("test123!@#"))
				.infoSet(InfoSet.DEFAULT)
				.build();

		User user2 = User.builder()
				.id(65L)
				.username("testUser65")
				.nickname("nickname65")
				.authentication(authentication2)
				.build();
		userRepository.save(user2);

		// when
		List<Long> userIds = Arrays.asList(64L, 65L);
		SuccessResponse response = userService.getUsersInfo(userIds);

		// then
		assertNotNull(response);
		assertEquals("유저 정보 조회 성공", response.getMessage());

		List<UserInfoResponseDtoV2> userInfoList = (List<UserInfoResponseDtoV2>) response.getData();
		assertEquals(2, userInfoList.size());
		assertEquals("testUser64", userInfoList.get(0).getUsername());
		assertEquals("testUser65", userInfoList.get(1).getUsername());
	}

	//	@Test
//	public void testSignup() {
//		// given
//		SignupRequestDto signupRequestDto = new SignupRequestDto("Test User", "Tester", "testUser", "password", "test@example.com");
//
//		user.service.entity.Authentication authentication = user.service.entity.Authentication.builder().userId(signupRequestDto.getUserId())
//				.email(signupRequestDto.getEmail()).password("encodedPassword").infoSet(InfoSet.DEFAULT).build();
//
//		User user = User.builder().username(signupRequestDto.getUsername()).role(Role.USER)
//				.nickname(signupRequestDto.getNickname()).build();
//		user.setAuthentication(authentication);
//		authentication.setUser(user);
//
//		when(bCryptPasswordEncoder.encode(signupRequestDto.getPassword())).thenReturn("encodedPassword");
//		when(authenticationRepository.saveAndFlush(any(user.service.entity.Authentication.class))).thenReturn(authentication);
//
//		// Mock UserRepository에 임의의 유저 설정
//		User savedUser = User.builder().username(signupRequestDto.getUsername()).role(Role.USER)
//				.nickname(signupRequestDto.getNickname()).build();
//		savedUser.setId(1L); // 임의 ID 설정
//		savedUser.setAuthentication(authentication);
//		when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);
//		// when
//		User result = userService.signup(signupRequestDto);
//
//		// then
//		assertNotNull(result);
//		assertEquals(signupRequestDto.getUsername(), result.getUsername());
//		assertEquals(signupRequestDto.getNickname(), result.getNickname());
//		verify(authenticationRepository, times(1)).saveAndFlush(any(user.service.entity.Authentication.class));
//		verify(userRepository, times(1)).saveAndFlush(any(User.class));
//	}
	@Test
	public void 로그인유저_정보() {
		// given setup()

		// when
		SuccessResponse response = userService.getUserInfo();

		// then
		assertNotNull(response);
		assertEquals("유저 정보 조회 성공", response.getMessage());

		UserInfoResponseDtoV1 userInfo = (UserInfoResponseDtoV1) response.getData();
		assertNotNull(userInfo);
		assertEquals(1, userInfo.getUserId());
	}
}
