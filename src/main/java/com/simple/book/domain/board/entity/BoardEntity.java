package com.simple.book.domain.board.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.simple.book.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity(name = "BOARD")
@DynamicInsert
@DynamicUpdate
@Builder
public class BoardEntity extends BaseTimeEntity{
	@Id
//	@NotNull
	@Column(name = "no")
	private long no;
	
//	@NotNull
	@Column(name = "id")
	private String id;

//	@NotNull
	@Column(name = "contents")
	private String contents;
	
	@Column(name = "image_path")
	private String imagePath;
	
	@Column(name = "video_path")
	private String videoPath;
	
	@Column(name="del_yn")
	private String delYn;

	@Column(name="hashtag")
	private String hashtag;

//	@NotNull
	@Column(name="ins_id")
	private String insId;
	
//	@NotNull
	@Column(name="upd_id")
	private String updId;
}
