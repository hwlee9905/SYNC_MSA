package com.simple.book.domain.board.dto;


import com.simple.book.domain.board.entity.BoardEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDto {
	@Schema(hidden = true)
	private long no;
	
	@Schema(hidden = true)
	private String id;
	
	@Schema(description = "게시물 내용", required = true)
	private String contents;
	
	@Schema(hidden = true)
	private String imagePath;
	
	@Schema(hidden = true)
	private String videoPath;
	
	@Schema(hidden = true)
	private String delYn;
	
	@Schema(description = "해시태그 내용", required = false)
	private String hashtag;
	
	@Schema(hidden = true)
	private String insId;
	
	@Schema(hidden = true)
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
