package project.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.service.entity.Task;
import project.service.entity.TaskImage;
import project.service.kafka.event.TaskCreateEvent;
import project.service.repository.TaskImageRepository;
@RequiredArgsConstructor
@Service
public class FileStorageService {
    @Value("${files.upload-dir.task.description}")
    private String uploadDescriptionDir;
    private final TaskImageRepository taskImageRepository;
    public void saveFiles(Task task, List<TaskCreateEvent.FileData> files) throws IOException {
        if (files != null) {
            for (TaskCreateEvent.FileData fileData : files) {
                String fileName = fileData.getFileName();
                Path copyLocation = Paths.get(uploadDescriptionDir + File.separator + fileName);
                Files.createDirectories(copyLocation.getParent());
                Files.write(copyLocation, fileData.getContent(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                TaskImage taskImage = TaskImage.builder()
                    .imagePath(copyLocation.toString())
                    .task(task)
                    .build();
                taskImageRepository.save(taskImage);
            }
        }
    }
}