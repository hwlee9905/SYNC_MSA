package com.simple.book.domain.board.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.simple.book.domain.board.repository.BoardRepository;
import com.simple.book.global.config.ApplicationConfig;
import com.simple.book.global.util.DateFmt;

import jakarta.servlet.http.HttpSession;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private DateFmt dateFmt;
	
	/**
	 * 글 목록
	 * @return
	 */
	public HashMap<String, Object> boardList(Principal principal){
		HashMap<String, Object> result = new HashMap<>();
		String id = principal.getName();
		
		return result;
	}
	
	/**
	 * 글 쓰기 (텍스트)
	 * @param session
	 * @param body
	 * @return
	 */
	public HashMap<String, Object> addBoard(Principal principal, HashMap<String, Object> body) {
		HashMap<String, Object> result = new HashMap<>();
		String id = principal.getName();
		String contents = String.valueOf(body.get("content"));
		String hashtag1 = String.valueOf(body.get("hashtag1"));
		String hashtag2 = String.valueOf(body.get("hashtag2"));
		String hashtag3 = String.valueOf(body.get("hashtag3"));
		String hashtag4 = String.valueOf(body.get("hashtag4"));
		String hashtag5 = String.valueOf(body.get("hashtag5"));
		
		
		
		
		
		
		return result;
	}

	/**
	 * 글 쓰기 (이미지 첨부)
	 * @param session
	 * @param file
	 * @return
	 */
	public HashMap<String, Object> imageUpload(MultipartFile file) {
		HashMap<String, Object> result = new HashMap<>();
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
