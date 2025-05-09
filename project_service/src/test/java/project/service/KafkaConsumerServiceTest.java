package project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.service.kafka.KafkaConsumerService;
import project.service.kafka.event.DeleteMemberFromTaskEvent;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class KafkaConsumerServiceTest {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @MockBean
    private TaskService taskService;

    @Test
    public void testListenDeleteFromMemberFromTaskEvent() {
        // Given
        DeleteMemberFromTaskEvent event = new DeleteMemberFromTaskEvent();
        event.setUserId(181L);
        event.setTaskId(281L);

        // When
        kafkaConsumerService.listenDeleteFromMemberFromTaskEvent(event);

        // Then
        verify(taskService, times(1)).removeUserFromTask(event);
    }
}