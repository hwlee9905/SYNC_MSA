package project.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.service.entity.Task;
import project.service.entity.TaskImage;
import project.service.kafka.event.TaskUpdateEvent;
import project.service.repository.TaskImageRepository;
@RequiredArgsConstructor
@Service
public class FileStorageService {
    @Value("${files.upload-dir.task.description}")
    private String uploadDescriptionDir;
    private final TaskImageRepository taskImageRepository;
    public <T extends project.service.global.FileData> void saveFiles(Task task, List<T> files) throws IOException {
        if (files != null) {
            for (T fileData : files) {
                String fileName = fileData.getFileName();
                Path copyLocation = Paths.get(uploadDescriptionDir + File.separator + fileName);
                Files.createDirectories(copyLocation.getParent());
                Files.write(copyLocation, fileData.getFileContent(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                TaskImage taskImage = TaskImage.builder()
                    .imagePath(copyLocation.toString())
                    .task(task)
                    .build();
                taskImageRepository.save(taskImage);
            }
        }
    }
    public void deleteFiles(List<TaskUpdateEvent.FileData> files) throws IOException {
        if (files != null) {
            for (TaskUpdateEvent.FileData fileData : files) {
                Path filePath = Paths.get(uploadDescriptionDir + File.separator + fileData.getFileName());
                Files.deleteIfExists(filePath);
            }
        }
    }
}