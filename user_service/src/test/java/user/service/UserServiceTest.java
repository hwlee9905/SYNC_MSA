package user.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import user.service.entity.Authentication;
import user.service.entity.InfoSet;
import user.service.entity.Role;
import user.service.entity.User;
import user.service.repository.AuthenticationRepository;
import user.service.repository.UserRepository;
import user.service.web.dto.request.SignupRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@Slf4j
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //실제 객체 주입
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                "Test User", "Tester", "testUser", "password", "test@example.com"
        );

        Authentication authentication = Authentication.builder()
                .userId(signupRequestDto.getUserId())
                .email(signupRequestDto.getEmail())
                .password("encodedPassword")
                .infoSet(InfoSet.DEFAULT)
                .build();

        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .role(Role.USER)
                .nickname(signupRequestDto.getNickname())
                .build();
        user.setAuthentication(authentication);
        authentication.setUser(user);

        when(bCryptPasswordEncoder.encode(signupRequestDto.getPassword())).thenReturn("encodedPassword");
        when(authenticationRepository.saveAndFlush(any(Authentication.class))).thenReturn(authentication);

        // Mock UserRepository에 임의의 유저 설정
        User savedUser = User.builder()
                .username(signupRequestDto.getUsername())
                .role(Role.USER)
                .nickname(signupRequestDto.getNickname())
                .build();
        savedUser.setId(1L); //임의 ID 설정
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
}
