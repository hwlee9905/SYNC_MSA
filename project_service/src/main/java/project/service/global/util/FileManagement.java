package project.service.global.util;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import project.service.global.config.ApplicationConfig;
import project.service.global.exception.DeleteImageFailedException;
import project.service.global.exception.ImageNotFoundException;
import project.service.global.exception.SavingImageFailedException;

@Component
@RequiredArgsConstructor
public class FileManagement {
	private final ApplicationConfig applicationConfig;
	
	// type: 'P' = 프로젝트, 'T' = 테스크
	
	public void uploadThumbnail(byte[] img, String imgName, char type) {
		File file;
		switch (type) {
			case 'P': 
				file = new File(applicationConfig.getProjectThumbnailStoragePath(), imgName);
				break;
			case 'T': 
				file = new File(applicationConfig.getTaskThumbnailStoragepath(), imgName);
				break;
			default:
				throw new IllegalArgumentException("'P'roject 또는 'T'ask만 허용 합니다.");
		}
		try (FileOutputStream fos = new FileOutputStream(file)) {
	        fos.write(img);
	        fos.flush();
	    } catch (Exception e) {
	        throw new SavingImageFailedException(e.getMessage());
		}
	}
	
	public void deleteThumbnail(String imgName, char type) {
		Path path;
		switch (type) {
			case 'P': 
				path = Paths.get(applicationConfig.getProjectThumbnailStoragePath(), imgName);
				break;
			case 'T': 
				path = Paths.get(applicationConfig.getTaskThumbnailStoragepath(), imgName);
				break;
			default:
				throw new IllegalArgumentException("'P'roject 또는 'T'ask만 허용 합니다.");
		}
		try {
            Files.delete(path);
        } catch (Exception e) {
        	throw new DeleteImageFailedException(e.getMessage());
        }
	}
	
	public Resource getThumbnail(String imgName, char type) {
		String path;
		switch (type) {
			case 'P': 
				path = applicationConfig.getProjectThumbnailStoragePath() + imgName;
				break;
			case 'T': 
				path = applicationConfig.getTaskThumbnailStoragepath() + imgName;
				break;
			default:
				throw new IllegalArgumentException("'P'roject 또는 'T'ask만 허용 합니다.");
		}
		
		Resource image = new FileSystemResource(path);
		if (!image.exists()) {
			throw new ImageNotFoundException(path);
		}
		return image;
	}
}
