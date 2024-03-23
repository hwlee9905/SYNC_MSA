package com.simple.book.domain.board.dto;

import com.simple.book.domain.board.entity.BoardEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {
	private long no;
	private String id;
	private String contents;
	private String imagePath;
	private String videoPath;
	private String delYn;
	private String hashtag1;
	private String hashtag2;
	private String hashtag3;
	private String hashtag4;
	private String hashtag5;
	private String insId;
	private String updId;

	@Builder
	public BoardDto(long no, String id, String contents, String imagePath, String videoPath, String delYn, String hashtag1, String hashtag2, String hashtag3, String hashtag4, String hashtag5, String insId, String updId ) {
		this.no=no;
		this.id=id;
		this.contents=contents;
		this.imagePath=imagePath;
		this.videoPath=videoPath;
		this.delYn=delYn;
		this.hashtag1=hashtag1;
		this.hashtag2=hashtag2;
		this.hashtag3=hashtag3;
		this.hashtag4=hashtag4;
		this.hashtag5=hashtag5;
		this.insId=insId;
		this.updId=updId;
	}

	public BoardEntity toEntity() {
		return BoardEntity.builder()
				.no(no)
				.id(id)
				.contents(contents)
				.imagePath(imagePath)
				.videoPath(videoPath)
				.delYn(delYn)
				.hashtag1(hashtag1)
				.hashtag2(hashtag2)
				.hashtag3(hashtag3)
				.hashtag4(hashtag4)
				.hashtag5(hashtag5)
				.insId(insId)
				.updId(updId)
				.build();
	}
}
