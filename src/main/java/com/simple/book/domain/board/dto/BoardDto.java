package com.simple.book.domain.board.dto;

import lombok.Data;

@Data
public class BoardDto {
	private int no;
	private String id;
	private String contents;
	private String imagePath;
	private String videoPath;
	private String delYn;
}
