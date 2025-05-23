package user.service.kafka.project;

import java.io.IOException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import user.service.MemberService;
import user.service.UserService;
import user.service.global.exception.ImageConversionFailedException;
import user.service.global.util.ExtsnFilter;
import user.service.kafka.project.event.ProjectCreateEvent;
import user.service.kafka.project.event.ProjectDeleteEvent;
import user.service.kafka.project.event.ProjectUpdateEvent;
import user.service.web.dto.project.request.CreateProjectRequestDto;
import user.service.web.dto.project.request.DeleteProjectRequestDto;
import user.service.web.dto.project.request.UpdateProjectRequestDto;

@Service
@RequiredArgsConstructor
public class KafkaProjectProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MemberService memberService;
    private final UserService userService;
    private final ExtsnFilter extsnFilter;

    private static final String TOPIC = "project-create-topic";
    private static final String TOPIC1 = "project-delete-topic";
    private static final String TOPIC2 = "project-update-topic";

    public void sendCreateProjectEvent(CreateProjectRequestDto projectCreateRequestDto, MultipartFile img, String userId) {
    	ProjectCreateEvent event = null;
        //이미지, 이모지, 아이콘 다 존재할경우 예외 처리해야함
    	/**
    	 * 'I' : 이미지
    	 * 'C' : 아이콘
    	 * 'E' : 이모지
    	 * 'N' : 없음
    	 */
        if (projectCreateRequestDto.getThumbnailType() == 'C' || 
        	projectCreateRequestDto.getThumbnailType() == 'E' || 
        	projectCreateRequestDto.getThumbnailType() == 'N') {							// 썸네일이 아이콘 또는 이모지 또는 없을 경우
            event = new ProjectCreateEvent(projectCreateRequestDto, null, null, userId);
        } else if (projectCreateRequestDto.getThumbnailType() == 'I') {						// 썸네일이 이미지일 경우
            byte[] imgByte = null;
            try {
                imgByte = img.getBytes();
            } catch (IOException e) {
                throw new ImageConversionFailedException(e.getMessage());
            }
            event = new ProjectCreateEvent(projectCreateRequestDto, imgByte, extsnFilter.getExtension(img), userId);
        } else {
        	// Exception
        }

        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
    }
    
    public void sendDeleteProjectEvent(DeleteProjectRequestDto projectDeleteRequestDto, String userId) {
        ProjectDeleteEvent event = new ProjectDeleteEvent(projectDeleteRequestDto.getProjectId(), userId);
        //프로젝트 생성자인지 확인
        memberService.isCreator(
            //프로젝트 멤버인지 확인
            memberService.findMemberByUserIdAndProjectId(
                userService.getUserEntityId(userId), projectDeleteRequestDto.getProjectId()
            )
            .getId()
        );
        //해당 프로젝트에 속한 멤버를 모두 삭제
        memberService.deleteMembersByProjectId(projectDeleteRequestDto.getProjectId());
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC1, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
    }
    
    public void updateProject(UpdateProjectRequestDto updateProjectRequestDto, MultipartFile img) {
    	ProjectUpdateEvent event = null;
        //이모지, 아이콘 둘다 존재할경우 예외 처리해야함
    	if (updateProjectRequestDto.getIcon() != null) {
            event = new ProjectUpdateEvent(updateProjectRequestDto, null, null);

    	} else if (img != null) {
            byte[] imgByte = null;
            try {
                imgByte = img.getBytes();
            } catch (IOException e) {
                throw new ImageConversionFailedException(e.getMessage());
            }
            event = new ProjectUpdateEvent(updateProjectRequestDto, imgByte, extsnFilter.getExtension(img));
    	} else {
            event = new ProjectUpdateEvent(updateProjectRequestDto, null, null);
        }
//        ProjectUpdateEvent event = new ProjectUpdateEvent(updateProjectRequestDto);
        //프로젝트 관리자인지 확인
        memberService.isManager(
            //프로젝트 멤버인지 확인
            memberService.findMemberByUserIdAndProjectId(
                userService.getUserEntityId(userService.getCurrentUserId()), updateProjectRequestDto.getProjectId()
            )
            .getId()
        );
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC2, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
    }
}