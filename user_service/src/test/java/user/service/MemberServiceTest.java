package user.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import user.service.repository.MemberRepository;
import user.service.web.MemberController;
import user.service.web.dto.member.request.MemberRemoveRequestDto;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

//실제 DB와 상호작용 하기 위해 Application Context를 로드
@SpringBootTest
public class MemberServiceTest {
    @Value("${spring.datasource.url}")
    private String DbUrl;
    @Value("${test.datasource.username.project}")
    private String ProjectDbUsername;
    @Value("${spring.datasource.password}")
    private String DbPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String DbDriverClassName;
    @InjectMocks
    private MemberController memberController;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    //Project Service DB 설정
    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry, MemberServiceTest memberServiceTest) {
        registry.add("spring.datasource.url", () -> memberServiceTest.DbUrl);
        registry.add("spring.datasource.username", () -> memberServiceTest.ProjectDbUsername);
        registry.add("spring.datasource.password", () -> memberServiceTest.DbPassword);
        registry.add("spring.datasource.driver-class-name", () -> memberServiceTest.DbDriverClassName);
    }

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testDeleteUsersFromTask() throws InterruptedException {
        // Given: 테스트에 필요한 데이터 설정
        Long taskId = 1L;
        MemberRemoveRequestDto memberRemoveRequestDto = MemberRemoveRequestDto.builder()
            .taskId(taskId)
            .userId("TestLoginID")
            .build();

        // When: API 호출하여 이벤트 발행
        memberController.deleteUsersFromTask(memberRemoveRequestDto);

        // Then: 이벤트가 처리될 때까지 대기 (예: 2초)
        Thread.sleep(2000);

        // 다른 서비스의 DB에서 해당 데이터가 올바르게 처리되었는지 검증
        String sql = "SELECT COUNT(*) FROM user_task WHERE task_id = ? AND user_id IN (?, ?)";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{taskId, 1L, 2L}, Integer.class);
        assertTrue(count == 0, "Task ID에 해당하는 멤버가 삭제되지 않았습니다.");
    }
}
