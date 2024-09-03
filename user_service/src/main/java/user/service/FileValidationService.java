package user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileValidationService {

    public void validateImageFile(MultipartFile file) throws IOException {
        validateExtension(file);
        validateUUIDPrefix(file);
    }

    private void validateExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    private void validateUUIDPrefix(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}_.*$")) {
            throw new IllegalArgumentException("파일명 앞에 UUID가 포함되어야 합니다.");
        }
    }
}