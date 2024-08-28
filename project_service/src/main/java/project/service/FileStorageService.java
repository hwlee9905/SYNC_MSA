package project.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.service.entity.Task;
import project.service.entity.TaskImage;
import project.service.repository.TaskImageRepository;
@RequiredArgsConstructor
@Service
public class FileStorageService {
    @Value("${files.upload-dir.task.description}")
    private String uploadDir;

    private final TaskImageRepository taskImageRepository;

    public void saveFiles(Task task, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            Path copyLocation = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            TaskImage taskImage = TaskImage.builder()
                    .imagePath(copyLocation.toString())
                    .task(task)
                    .build();
            taskImageRepository.save(taskImage);
        }
    }
}