package com.simple.book.domain.board.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.simple.book.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity(name = "BOARDTBL")
@DynamicUpdate
@Builder
public class BoardEntity extends BaseTimeEntity{
	@Id
	@Column(name = "NO")
	private long no;
	
	@Column(name = "ID")
	private String id;
	
	@Column(name = "CONTENTS")
	private String contents;
	
	@Column(name = "IMAGE_PATH")
	private String imagePath;
	
	@Column(name = "VIDEO_PATH")
	private String videoPath;
	
	@Column(name="DEL_YN")
	private String delYn;
	
	@Column(name="HASHTAG_1")
	private String hashtag1;
	
	@Column(name="HASHTAG_2")
	private String hashtag2;
	
	@Column(name="HASHTAG_3")
	private String hashtag3;
	
	@Column(name="HASHTAG_4")
	private String hashtag4;
	
	@Column(name="HASHTAG_5")
	private String hashtag5;
	
	@Column(name = "INS_ID")
	private String insId;
	
	@Column(name = "UPD_ID")
	private String updId;
	
}
