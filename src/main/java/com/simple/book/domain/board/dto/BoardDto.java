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
	private String hashtag;
	private String insId;
	private String updId;

	@Builder
	public BoardDto(long no, String id, String contents, String imagePath, String videoPath, String delYn, String hashtag, String insId, String updId ) {
		this.no=no;
		this.id=id;
		this.contents=contents;
		this.imagePath=imagePath;
		this.videoPath=videoPath;
		this.delYn=delYn;
		this.hashtag=hashtag;
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
				.hashtag(hashtag)
				.insId(insId)
				.updId(updId)
				.build();
	}
}
