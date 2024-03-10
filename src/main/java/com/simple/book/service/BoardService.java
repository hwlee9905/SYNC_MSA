package com.simple.book.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.simple.book.config.ApplicationConfig;
import com.simple.book.repository.BoardRepository;
import com.simple.book.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private DateFmt dateFmt;
	
	public HashMap<String, Object> boardList(){
		HashMap<String, Object> result = new HashMap<>();
		
		return result;
	}
	
	public HashMap<String, Object> addBoard(HttpSession session, HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {

		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	public HashMap<String, Object> addBoard(HttpSession session, MultipartFile file) {
		HashMap<String, Object> result = new HashMap<>();
		Object id = session.getAttribute("id");
		if (id != null) {
			Path uploadPath = Paths.get(applicationConfig.getImagePath());
			if (Files.exists(uploadPath)) {
				String fileName = StringUtils.cleanPath(createFilename(file));
				Path filePath = uploadPath.resolve(fileName);
				try {
					Files.copy(file.getInputStream(), filePath);
				} catch (Exception e) {
					e.getStackTrace();
				}
				result.put("result", true);
			} else {
				// 디렉터리 오류
			}
		} else {
			result.put("result", "no_session");
		}
		return result;
	}

	private String createFilename(MultipartFile file) {
		String date = dateFmt.getDate("yyyyMMdd");
		String time = dateFmt.getDate("HHmmss");
		UUID uuid = UUID.randomUUID();
		String customUUid = uuid.toString().substring(0, 8);
		String extension = getExtension(file);
		return date + time + "_" + customUUid + "." + extension;
	}

	private String getExtension(MultipartFile file) {
		String result = "";
		String filename = file.getOriginalFilename();
		if (filename != null) {
			int lastIndex = filename.lastIndexOf(".");
			if (lastIndex != -1 && lastIndex < filename.length() - 1) {
				result = filename.substring(lastIndex + 1);
			}
		} else {
			// 파일이 없음
		}

		return result;
	}
}
