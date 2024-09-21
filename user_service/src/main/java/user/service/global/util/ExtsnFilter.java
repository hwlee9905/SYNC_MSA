package user.service.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import user.service.global.exception.InvalidFileExtensionException;

@Component
public class ExtsnFilter {
	public String getExtension(MultipartFile img) {
    	String filename = img.getOriginalFilename();
    	
		int dotIndex = filename.lastIndexOf('.');
		        
		if (dotIndex > 0 && dotIndex < filename.length() - 1) {
			return filename.substring(dotIndex + 1).toLowerCase();
		} else {
			throw new InvalidFileExtensionException();
		}
    }
}
