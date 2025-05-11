package user.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import user.service.jwt.util.JWTUtil;

@SpringBootTest
public class JWTFilterTest {

    @SpyBean
    private JWTUtil jwtUtil;

    @Test
    void testJwtCreation() {

        String token = jwtUtil.createJwt("testUser", "USER", 60*30*1000L, "NAVER", "Test User");
        Assertions.assertNotNull(token);
        Assertions.assertFalse(jwtUtil.isExpired(token));
        Assertions.assertEquals("testUser", jwtUtil.getUsername(token));
    }
}
