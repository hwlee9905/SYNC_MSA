package com.simple.book.domain.board.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.simple.book.domain.board.dto.BoardDto;
import com.simple.book.domain.board.entity.BoardEntity;
import com.simple.book.domain.board.repository.BoardRepository;
import com.simple.book.global.config.ApplicationConfig;
import com.simple.book.global.util.DateFmt;
import com.simple.book.global.util.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private DateFmt dateFmt;
	
	/**
	 * 글쓰기 (텍스트)
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public ResponseMessage addBoard(BoardDto body, MultipartFile file) throws Exception {
		if (body.getContents() != null && !body.getContents().isEmpty()) {
			System.out.println(file);
			Optional<BoardEntity> result = Optional.ofNullable(boardRepository.save(body.toEntity()));
			result.orElseThrow(() -> new RuntimeException("system_error"));
		} else throw new RuntimeException("본문을 입력 해 주세요.");
		
 		return ResponseMessage.builder().message("저장 완료").build();
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
