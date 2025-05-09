package user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import user.service.entity.Authentication;
import user.service.entity.InfoSet;
import user.service.entity.Role;
import user.service.entity.User;
import user.service.repository.AuthenticationRepository;
import user.service.repository.UserRepository;
import user.service.web.dto.request.SignupRequestDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AuthenticationRepository authenticationRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@MockBean
	private UserService userService;

	@BeforeEach
	void setUp() {
		// 테스트용 유저 저장
		User user = User.builder()
				.username("testuser")
				.role(Role.USER)
				.position("backend")
				.nickname("Tester")
				.build();
		Authentication auth = Authentication.builder()
				.infoSet(InfoSet.DEFAULT)
				.userId("testuser")
				.password(new BCryptPasswordEncoder().encode("testpass"))
				.user(user)
				.build();
		user.setAuthentication(auth);

		when(authenticationRepository.findByUserId("testuser")).thenReturn(auth);
		when(userRepository.findById(any())).thenReturn(Optional.of(user)); // 필요하다면
		when(bCryptPasswordEncoder.matches(eq("testpass"), any())).thenReturn(true);
	}

	@Test
	void 로그인후_JWT쿠키포함하여_유저정보조회_성공() {
		// 1. 로그인 요청
		HttpHeaders loginHeaders = new HttpHeaders();
		loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
		loginBody.add("id", "testuser");
		loginBody.add("password", "testpass");

		HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(loginBody, loginHeaders);

		ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginRequest, String.class);
		assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. JWT 쿠키 추출
		List<String> setCookieHeaders = loginResponse.getHeaders().get("Set-Cookie");
		assertThat(setCookieHeaders).isNotEmpty();

		String jwtCookie = setCookieHeaders.stream()
				.filter(cookie -> cookie.startsWith("JWT_TOKEN"))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("JWT 토큰 쿠키가 없습니다."));

		HttpHeaders infoHeaders = new HttpHeaders();
		infoHeaders.add("Cookie", jwtCookie);

		HttpEntity<Void> userInfoRequest = new HttpEntity<>(infoHeaders);

		// 3. JWT 쿠키로 유저 정보 조회
		ResponseEntity<String> userInfoResponse = restTemplate.exchange(
				"/user/api/info/v1", HttpMethod.GET, userInfoRequest, String.class);

		assertThat(userInfoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(userInfoResponse.getBody()).contains("유저 정보 조회 성공");
		assertThat(userInfoResponse.getBody()).contains("testuser");
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
//	@Test
//	public void testGetCurrentUserInfo() {
//		// given
//		String userId = "testUser";
//		User user = new User();
//		user.setId(1L);
//		user.setUsername("Test User");
//		user.setNickname("Tester");
//		user.setPosition("Developer");
//
//		// when
//		when(userDetails.getUsername()).thenReturn(userId);
//		when(userRepository.findByAuthenticationUserId(userId)).thenReturn(user);
//		SuccessResponse response = userService.getUserInfo();
//
//		// then
//		assertNotNull(response);
//		assertEquals("유저 정보 조회 성공", response.getMessage());
//
//		UserInfoResponseDtoV1 userInfo = (UserInfoResponseDtoV1) response.getData();
//		assertNotNull(userInfo);
//		assertEquals("Test User", userInfo.getUsername());
//		assertEquals("Tester", userInfo.getNickname());
//		assertEquals("Developer", userInfo.getPosition());
//	}
}
